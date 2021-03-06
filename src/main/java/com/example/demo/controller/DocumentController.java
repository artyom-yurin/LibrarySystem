package com.example.demo.controller;

import com.example.demo.common.Privileges;
import com.example.demo.entity.document.*;
import com.example.demo.exception.*;
import com.example.demo.model.DocumentModel;
import com.example.demo.repository.TagRepository;
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
import java.util.stream.*;

@RestController
public class DocumentController {

    @Autowired
    private BookingController bookingController;

    private DocumentService documentService;
    private TypeDocumentService typeDocumentService;
    private AuthorService authorService;
    private PublisherService publisherService;
    private LogService logService;
    private TagService tagService;

    public DocumentController(DocumentService documentService, TypeDocumentService typeDocumentService, AuthorService authorService, PublisherService publisherService, LogService logService, TagService tagService) {
        this.documentService = documentService;
        this.typeDocumentService = typeDocumentService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.logService = logService;
        this.tagService = tagService;
    }

    /**
     * Internal method for finding authors from the given set
     * @param setAuthors Set of authors
     * @return HashSet of all authors from given set currently in the system
     */
    private HashSet<Author> findAuthors(Set<Author> setAuthors) {
        HashSet<Author> authors = new HashSet<>();
        if(setAuthors != null && setAuthors.size() > 0)
        {
            for (Author authorItem : setAuthors) {
                List<Author> concurrencesAuthors = authorService.findAll()
                        .stream()
                        .filter(author -> author.getFirstName().toLowerCase().equals(authorItem.getFirstName().toLowerCase()))
                        .filter(author -> author.getLastName().toLowerCase().equals(authorItem.getLastName().toLowerCase()))
                        .collect(Collectors.toList());
                if(concurrencesAuthors.isEmpty())
                {
                    authorService.save(new Author(authorItem.getFirstName(), authorItem.getLastName()));
                    concurrencesAuthors = authorService.findAll()
                            .stream()
                            .filter(author -> author.getFirstName().toLowerCase().equals(authorItem.getFirstName().toLowerCase()))
                            .filter(author -> author.getLastName().toLowerCase().equals(authorItem.getLastName().toLowerCase()))
                            .collect(Collectors.toList());
                }
                authors.add(concurrencesAuthors.get(0));
            }
        }
        return authors;
    }

    private HashSet<Tag> findTags(Set<Tag> setTags) {
        HashSet<Tag> tags = new HashSet<>();
        if(setTags != null && setTags.size() > 0)
        {
            for (Tag tagItem : setTags) {
                Tag newTag = tagService.findTag(tagItem.getTagName().toLowerCase());
                if(newTag == null)
                {
                    tagService.save(tagItem);
                    newTag = tagService.findTag(tagItem.getTagName().toLowerCase());
                }
                tags.add(newTag);
            }
        }
        return tags;
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
        Publisher publisher = null;
        Set<Tag> tags = findTags(documentModel.getTags());
        if(!"avmaterial".equals(type.getTypeName()))
        {
            publisher = publisherService.findByPublisherName(documentModel.getPublisher());
            if (publisher == null)
            {
                publisherService.save(documentModel.getPublisher());
                publisher = publisherService.findByPublisherName(documentModel.getPublisher());
            }
        }

        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), documentModel.getCount(), tags, publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), type);
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
        Set<Tag> tags = findTags(documentModel.getTags());
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
        if (Privileges.Privilege.Priv3.compareTo(Privileges.convertStringToPrivelege(token.position)) <= 0)
        {
            count = documentModel.getCount();
            if (count < 0)
            {
                throw new InvalidCountException();
            }
            needAllocation = true;
        }
        Document document = new Document(documentModel.getTitle(), authors, documentModel.getPrice(), count, tags, publisher, documentModel.getEdition(), documentModel.isBestseller(), documentModel.isReference(), documentModel.getPublishingDate(), documentModel.getEditor(), documentModel.getType());
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
