package com.example.demo.controller;

import com.example.demo.common.Privileges;
import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.TypeDocument;
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
import java.util.Set;

@RestController
public class DocumentController {

    @Autowired
    private BookingController bookingController;

    private DocumentService documentService;
    private TypeDocumentService typeDocumentService;
    private AuthorService authorService;
    private PublisherService publisherService;
    private LogService logService;

    public DocumentController(DocumentService documentService, TypeDocumentService typeDocumentService, AuthorService authorService, PublisherService publisherService, LogService logService) {
        this.documentService = documentService;
        this.typeDocumentService = typeDocumentService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.logService = logService;
    }

    /**
     * Internal method for finding authors from the given set
     * @param setAuthors Set of authors
     * @return HashSet of all authors from given set currently in the system
     */
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

    /**
     * Method for adding a new document
     * @param documentModel Model of the document (internal representation)
     * @param request HTTP Servlet Request with a token of the session
     */
    @PostMapping("/document/add")
    public void addDocument(@RequestBody DocumentModel documentModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv2.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0) throw new AccessDeniedException();

        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = findAuthors(documentModel.getAuthors());
        Publisher publisher;
        if (documentModel.getPublisher().getId() != null) {
            publisher = publisherService.findById(documentModel.getPublisher().getId());
        } else {
            publisher = publisherService.findByPublisherName(documentModel.getPublisher().getPublisherName());
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), type);
        this.documentService.save(document);

        logService.newLog(token.id, "Added new document " + document.getTitle());
    }

    /**
     * Method for updating the document
     * @param documentModel Model of the document (internal representation)
     * @param request HTTP Servlet Request with a token of the session
     */
    @PutMapping("/document/update")
    public void updateDocument(@RequestBody DocumentModel documentModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv1.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0) throw new AccessDeniedException();

        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = findAuthors(documentModel.getAuthors());
        Publisher publisher;
        if (documentModel.getPublisher().getId() != null) {
            publisher = publisherService.findById(documentModel.getPublisher().getId());
        } else {
            publisher = publisherService.findByPublisherName(documentModel.getPublisher().getPublisherName());
        }

        int count = documentService.findById(documentModel.getId()).getCount();
        boolean needAllocation = false;
        if (Privileges.Privilege.Priv3.compareTo(Privileges.convertStringToPrivelege(token.position)) <= 0)
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

        logService.newLog(token.id, "Updated document id " + documentModel.getId());
    }

    /**
     * Method for removing the document given its ID
     * @param id ID of the document to remove
     * @param request HTTP Servlet Request with a token of the session
     */
    @Transactional
    @DeleteMapping("/document/remove")
    public void removeDocumentId(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv3.compareTo(Privileges.convertStringToPrivelege(token.position)) > 0) throw new AccessDeniedException();

        if (id == -1)
            throw new InvalidIdException();

        Document document = documentService.findById(id);

        if (document == null)
            throw new DocumentNotFoundException();

        this.documentService.remove(id);

        logService.newLog(token.id, "Removed document" + document.getTitle());
    }

    @GetMapping("/document/find")
    public Document getDocument(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();

        if (id == -1)
            throw new InvalidIdException();

        Document findDocument = documentService.findById(id);
        if (findDocument == null) throw new DocumentNotFoundException();
        return findDocument;
    }

    /**
     * Method for displaying all registered documents in the system
     * @param request HTTP Servlet Request with a token of the session
     * @return List of all documents currently in the system
     */
    @GetMapping("/document/documents")
    public Iterable<Document> getDocuments(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        return this.documentService.getAllDocuments();
    }
}
