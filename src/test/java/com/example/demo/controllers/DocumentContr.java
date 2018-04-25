package com.example.demo.controllers;

import com.example.demo.common.Privileges;
import com.example.demo.entity.document.*;
import com.example.demo.entity.user.User;
import com.example.demo.exception.*;
import com.example.demo.model.DocumentModel;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private TagService tagService;

    public DocumentContr(DocumentService documentService, TypeDocumentService typeDocumentService, AuthorService authorService, PublisherService publisherService, LogService logService, TagService tagService) {
        this.documentService = documentService;
        this.typeDocumentService = typeDocumentService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.logService = logService;
        this.tagService = tagService;
    }

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

    public void addDocument(DocumentModel documentModel, Integer librarianId) {
        User librarian = userService.findById(librarianId);
        if (!librarian.getRole().getName().equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv2.compareTo(Privileges.convertStringToPrivelege(librarian.getRole().getPosition())) > 0) throw new AccessDeniedException();

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

        logService.newLog(librarianId, "Added new document " + document.getTitle());
    }

    public void updateDocument(DocumentModel documentModel, Integer librarianId) {
        User librarian = userService.findById(librarianId);
        if (!librarian.getRole().getName().equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv1.compareTo(Privileges.convertStringToPrivelege(librarian.getRole().getPosition())) > 0) throw new AccessDeniedException();

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
        if (Privileges.Privilege.Priv3.compareTo(Privileges.convertStringToPrivelege(librarian.getRole().getPosition())) <= 0)
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

        logService.newLog(librarianId, "Updated document id " + documentModel.getId());
    }


    public void removeDocumentId(Integer id, Integer librarianId) {
        User librarian = userService.findById(librarianId);
        if (!librarian.getRole().getName().equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv3.compareTo(Privileges.convertStringToPrivelege(librarian.getRole().getPosition())) > 0) throw new AccessDeniedException();

        if (id == -1)
            throw new InvalidIdException();

        Document document = documentService.findById(id);

        if (document == null)
            throw new UserNotFoundException();

        this.documentService.remove(id);

        logService.newLog(librarianId, "Removed document" + document.getTitle());
    }

    public void removeCopy(Integer id, Integer librarianId) {
        User librarian = userService.findById(librarianId);
        if (!librarian.getRole().getName().equals("librarian")) throw new AccessDeniedException();
        if (Privileges.Privilege.Priv2.compareTo(Privileges.convertStringToPrivelege(librarian.getRole().getPosition())) > 0) throw new AccessDeniedException();

        if (id == -1)
            throw new InvalidIdException();

        Document document = documentService.findById(id);

        if (document == null)
            throw new UserNotFoundException();

        document.setCount(document.getCount() - 1);

        this.documentService.save(document);
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
