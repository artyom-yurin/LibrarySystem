package com.example.demo.controllers;

import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.TypeDocument;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.exception.DocumentNotFoundException;
import com.example.demo.exception.TypeNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.DocumentModel;
import com.example.demo.service.AuthorService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.PublisherService;
import com.example.demo.service.TypeDocumentService;
import com.example.security.ParserToken;
import com.example.security.TokenAuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Controller
public class DocumentContr {

    private DocumentService documentService;
    private TypeDocumentService typeDocumentService;
    private AuthorService authorService;
    private PublisherService publisherService;

    public DocumentContr(DocumentService documentService, TypeDocumentService typeDocumentService, AuthorService authorService, PublisherService publisherService) {
        this.documentService = documentService;
        this.typeDocumentService = typeDocumentService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    public void addDocumentTest(DocumentModel documentModel) {
        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = new HashSet<>();
        if (documentModel.getAuthors() != null) {
            for (Author author : documentModel.getAuthors()) {
                if (author.getId() != null) {
                    authors.add(authorService.findById(author.getId()));
                } else if (author.getFirstName() != null) {
                    authors.add(authorService.findByFirstName(author.getFirstName()));
                } else {
                    authors.add(authorService.findByLastName(author.getLastName()));
                }
            }
        }
        Publisher publisher = null;
        if (documentModel.getPublisher() != null) {
            if (documentModel.getPublisher().getId() != null) {
                publisher = publisherService.findById(documentModel.getPublisher().getId());
            } else {
                publisher = publisherService.findByPublisherName(documentModel.getPublisher().getPublisherName());
            }
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), type);
        document.setId(-1);
        this.documentService.save(document);
    }

    public void updateDocumentTest(DocumentModel documentModel) {
        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = new HashSet<>();
        if (documentModel.getAuthors() != null) {
            for (Author author : documentModel.getAuthors()) {
                if (author.getId() != null) {
                    authors.add(authorService.findById(author.getId()));
                } else if (author.getFirstName() != null) {
                    authors.add(authorService.findByFirstName(author.getFirstName()));
                } else {
                    authors.add(authorService.findByLastName(author.getLastName()));
                }
            }
        }
        Publisher publisher;
        if (documentModel.getPublisher().getId() != null) {
            publisher = publisherService.findById(documentModel.getPublisher().getId());
        } else {
            publisher = publisherService.findByPublisherName(documentModel.getPublisher().getPublisherName());
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), documentModel.getType());
        document.setId(documentModel.getId());
        this.documentService.save(document);
    }

    public void removeDocumentTest(Document document) {
        this.documentService.remove(document.getId());
    }

    public void removeDocumentIdTest(Integer id) {
        this.documentService.remove(documentService.findById(id).getId());
    }

    public Document getDocumentTest(Integer id) {
        Document findDocument = documentService.findById(id);
        if (findDocument == null) throw new DocumentNotFoundException();
        return findDocument;
    }

    public Iterable<Document> getDocumentsTest() {
        return this.documentService.getAllDocuments();
    }

    public int getAmountTest() {
        int count = 0;
        for (Document document : getDocumentsTest()) {
            count += document.getCount();
        }
        return count;
    }
}
