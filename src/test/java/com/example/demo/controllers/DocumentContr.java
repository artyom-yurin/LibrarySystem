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

    @PostMapping("/document/add")
    public void addDocument(@RequestBody DocumentModel documentModel, HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if(type == null) throw new TypeNotFoundException();
        Set<Author> authors = new HashSet<>();
        if(documentModel.getAuthors() != null) {
            for (Author author : documentModel.getAuthors()) {
                if(author.getId() != null){
                    authors.add(authorService.findById(author.getId()));
                }
                else if(author.getFirstName() != null){
                    authors.add(authorService.findByFirstName(author.getFirstName()));
                }
                else{ authors.add(authorService.findByLastName(author.getLastName())); }
            }
        }
        Publisher publisher;
        if(documentModel.getPublisher().getId() != null) {
            publisher = publisherService.findById(documentModel.getPublisher().getId());
        }
        else{
            publisher = publisherService.findByPublisherName(documentModel.getPublisher().getPublisherName());
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), type);
        this.documentService.save(document);
    }

    @PutMapping("/document/update")
    public void updateDocument(@RequestBody DocumentModel documentModel, HttpServletRequest request)
    {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        TypeDocument type = typeDocumentService.findByTypeName(documentModel.getType().getTypeName());
        if (type == null) throw new TypeNotFoundException();
        Set<Author> authors = new HashSet<>();
        if(documentModel.getAuthors() != null) {
            for (Author author : documentModel.getAuthors()) {
                if(author.getId() != null){
                    authors.add(authorService.findById(author.getId()));
                }
                else if(author.getFirstName() != null){
                    authors.add(authorService.findByFirstName(author.getFirstName()));
                }
                else{ authors.add(authorService.findByLastName(author.getLastName())); }
            }
        }
        Publisher publisher;
        if(documentModel.getPublisher().getId() != null) {
            publisher = publisherService.findById(documentModel.getPublisher().getId());
        }
        else{
            publisher = publisherService.findByPublisherName(documentModel.getPublisher().getPublisherName());
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), documentModel.getTags(), publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), documentModel.getType());
        document.setId(documentModel.getId());
        this.documentService.save(document);
    }

    @Transactional
    @DeleteMapping("/document/remove")
    public void removeDocument(@RequestBody Document document, HttpServletRequest request)
    {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        this.documentService.remove(document.getId());
    }

    @Transactional
    @DeleteMapping("/document/removeid")
    public void removeDocumentId(@RequestParam(value = "id", defaultValue = "") Integer id, HttpServletRequest request)
    {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        if (!token.role.equals("admin")) throw new AccessDeniedException();

        this.documentService.remove(documentService.findById(id).getId());
    }

    @GetMapping("/document/find")
    public Document getDocument(@RequestParam(value = "id", defaultValue = "") Integer id, HttpServletRequest request)
    {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();

        Document findDocument = documentService.findById(id);
        if (findDocument == null) throw new DocumentNotFoundException();
        return findDocument;
    }

    @GetMapping("/document/documents")
    public Iterable<Document> getDocuments(HttpServletRequest request){
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null) throw new UnauthorizedException();
        return this.documentService.getAllDocuments();
    }
}
