package com.example.demo.controllers;

import com.example.demo.controller.BookingContr;
import com.example.demo.entity.Notification;
import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.Tag;
import com.example.demo.entity.user.User;
import com.example.demo.model.DocumentModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.*;
import com.example.demo.service.TypeDocumentService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class SystemTest {
    @Autowired
    UserContr userController;
    @Autowired
    DocumentContr documentController;
    @Autowired
    BookingContr bookingController;
    @Autowired
    SystemContr systemController;
    @Autowired
    NotificationContr notificationController;

    @Autowired
    TypeDocumentService typeDocumentService;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    PublisherRepository publisherRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    BookingRepository bookingRepository;

    @Before
    @After
    public void clearDB() {
        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
        notificationRepository.deleteAll();
    }

    public void addUsers()
    {
        userController.addUser(new UserModel(-1, "Sergey", "Afonso", "Via Margutta, 3", "30001", "Professor", "ser", "123"));

        userController.addUser(new UserModel(-1, "Nadia", "Teixeira", "Via Sacra, 13", " 30002", "Professor", "nad", "123"));

        userController.addUser(new UserModel(-1, "Elvira", "Espindola", "Via del Corso, 22", "30003", "Professor", "elv", "123"));

        userController.addUser(new UserModel(-1, "Andrey", "Velo", "Avenida Mazatlan 250", "30004", "Student", "and", "123"));

        userController.addUser(new UserModel(-1, "Veronika", "Rama", "Stret Atocha, 27", "30005", "VP", "ver", "123"));
    }

    public void addDocuments()
    {
        Set<Author> authors0 = new HashSet<>();
        Author tempAuthor = new Author("Thomas H", "Cormen");
        authorRepository.save(tempAuthor);
        authors0.add(authorRepository.findByLastName("Cormen"));

        tempAuthor = new Author("Charles E", "Leiserson");
        authorRepository.save(tempAuthor);
        authors0.add(authorRepository.findByLastName("Leiserson"));

        tempAuthor = new Author("Ronald L", "Rivest");
        authorRepository.save(tempAuthor);
        authors0.add(authorRepository.findByLastName("Rivest"));

        tempAuthor = new Author("Clifford", "Stein");
        authorRepository.save(tempAuthor);
        authors0.add(authorRepository.findByLastName("Stein"));

        Publisher publisher0 = new Publisher(("MIT Press").toLowerCase());
        publisherRepository.save(publisher0);
        publisher0 = publisherRepository.findByPublisherName(("MIT Press").toLowerCase());
        documentController.addDocument(new DocumentModel(1, "Introduction to Algorithms", authors0, 5000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors1 = new HashSet<>();
        tempAuthor = new Author("Erich", "Gamma");
        authorRepository.save(tempAuthor);
        authors1.add(authorRepository.findByLastName("Gamma"));

        tempAuthor = new Author("Ralph", "Johnson");
        authorRepository.save(tempAuthor);
        authors1.add(authorRepository.findByLastName("Johnson"));

        tempAuthor = new Author("John", "Vlissides");
        authorRepository.save(tempAuthor);
        authors1.add(authorRepository.findByLastName("Vlissides"));

        tempAuthor = new Author("Richard", "Helm");
        authorRepository.save(tempAuthor);
        authors1.add(authorRepository.findByLastName("Helm"));

        Publisher publisher1 = new Publisher("Addison-Wesley Professional".toLowerCase());
        publisherRepository.save(publisher1);
        publisher1 = publisherRepository.findByPublisherName("Addison-Wesley Professional".toLowerCase());
        documentController.addDocument(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Software", authors1, 1700, 3, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors3 = new HashSet<>();
        tempAuthor = new Author("Tony", "Hoare");
        authorRepository.save(tempAuthor);
        authors3.add(authorRepository.findByLastName("Hoare"));

        documentController.addDocument(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 700, 2, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));
    }


    @Test
    public void test1() {
        addUsers();
        addDocuments();

        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
        notificationRepository.deleteAll();
    }
}