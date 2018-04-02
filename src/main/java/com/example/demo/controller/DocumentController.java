package com.example.demo.controller;

import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.TypeDocument;
import com.example.demo.exception.*;
import com.example.demo.model.DocumentModel;
import com.example.demo.service.AuthorService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.PublisherService;
import com.example.demo.service.TypeDocumentService;
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

    public DocumentController(DocumentService documentService, TypeDocumentService typeDocumentService, AuthorService authorService, PublisherService publisherService) {
        this.documentService = documentService;
        this.typeDocumentService = typeDocumentService;
        this.authorService = authorService;
        this.publisherService = publisherService;
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

    @PostMapping("/document/add")
    public void addDocument(@RequestBody DocumentModel documentModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

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
    }

    @PutMapping("/document/update")
    public void updateDocument(@RequestBody DocumentModel documentModel, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = findAuthors(documentModel.getAuthors());
        Publisher publisher;
        if (documentModel.getPublisher().getId() != null) {
            publisher = publisherService.findById(documentModel.getPublisher().getId());
        } else {
            publisher = publisherService.findByPublisherName(documentModel.getPublisher().getPublisherName());
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), documentModel.getType());
        document.setId(documentModel.getId());
        this.documentService.save(document);
        bookingController.queueAllocation(document.getId());
    }

    @Transactional
    @DeleteMapping("/document/remove")
    public void removeDocumentId(@RequestParam(value = "id", defaultValue = "-1") Integer id, HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        if (id == -1)
            throw new InvalidIdException();

        Document document = documentService.findById(id);

        if (document == null)
            throw new UserNotFoundException();

        this.documentService.remove(id);
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

    @GetMapping("/document/documents")
    public Iterable<Document> getDocuments(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        return this.documentService.getAllDocuments();
    }
}
