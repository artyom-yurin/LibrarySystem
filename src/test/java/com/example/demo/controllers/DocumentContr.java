package com.example.demo.controllers;

import com.example.demo.common.Privileges;
import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.TypeDocument;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.model.DocumentModel;
import com.example.demo.service.*;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class DocumentContr {

    @Autowired
    private BookingContr bookingController;
    @Autowired
    private UserService userService;

    private DocumentService documentService;
    private TypeDocumentService typeDocumentService;
    private AuthorService authorService;
    private PublisherService publisherService;
    private LogService logService;

    public DocumentContr(DocumentService documentService, TypeDocumentService typeDocumentService, AuthorService authorService, PublisherService publisherService, LogService logService) {
        this.documentService = documentService;
        this.typeDocumentService = typeDocumentService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.logService = logService;
    }

    private HashSet<Author> findAuthors(Set<Author> setAuthors) {
        HashSet<Author> authors = new HashSet<>();
        if (setAuthors != null) {
            for (Author author : setAuthors) {
                if (author.getId() != null) {
                    authors.add(authorService.findById(author.getId()));
                } else if (author.getFirstName() != null) {
                    authors.add(authorService.findByFirstName(author.getFirstName()));
                } else {
                    authors.add(authorService.findByLastName(author.getLastName()));
                }
            }
        }
        return authors;
    }

    public void addDocument(DocumentModel documentModel, Integer librarianId) {
        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = findAuthors(documentModel.getAuthors());
        Publisher publisher = null;

        if(!"avmaterial".equals(type.getTypeName()))
        {
            publisher = publisherService.findByPublisherName(documentModel.getPublisher());
            if (publisher == null)
            {
                publisherService.save(documentModel.getPublisher());
                publisher = publisherService.findByPublisherName(documentModel.getPublisher());
            }
        }

        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), type);
        this.documentService.save(document);

        logService.newLog(librarianId, "Added new document " + document.getTitle());
    }

    public void updateDocument(DocumentModel documentModel, Integer librarianId) {
        User librarian = userService.findById(librarianId);

        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = findAuthors(documentModel.getAuthors());

        Publisher publisher = null;
        if(!"avmaterial".equals(type.getTypeName()))
        {
            publisher = publisherService.findByPublisherName(documentModel.getPublisher());
            if (publisher == null)
            {
                publisherService.save(documentModel.getPublisher());
            }
        }

        int count = documentService.findById(documentModel.getId()).getCount();
        boolean needAllocation = false;
        if (Privileges.Privilege.Priv3.compareTo(Privileges.convertStringToPrivelege(librarian.getRole().getPosition())) <= 0)
        {
            count = documentModel.getCount();
            if (count < 0)
            {
                throw new InvalidCountException();
            }
            needAllocation = true;
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), count, documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), documentModel.getType());
        document.setId(documentModel.getId());
        this.documentService.save(document);
        if (needAllocation) bookingController.queueAllocation(document.getId());

        logService.newLog(librarianId, "Updated document id " + documentModel.getId());
    }


    public void removeDocumentId(Integer id, Integer librarianId) {
        if (id == -1)
            throw new InvalidIdException();

        Document document = documentService.findById(id);

        if (document == null)
            throw new UserNotFoundException();

        this.documentService.remove(id);

        logService.newLog(librarianId, "Removed document" + document.getTitle());
    }

    public Document getDocument(Integer id) {

        if (id == -1)
            throw new InvalidIdException();

        Document findDocument = documentService.findById(id);
        if (findDocument == null) throw new DocumentNotFoundException();
        return findDocument;
    }

    public List<Document> getDocuments() {
        return documentService.getAllDocuments();
    }
}
