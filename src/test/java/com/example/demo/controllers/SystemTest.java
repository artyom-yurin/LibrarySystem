package com.example.demo.controllers;

import com.example.demo.entity.booking.Booking;
import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.Tag;
import com.example.demo.entity.user.User;
import com.example.demo.model.DocumentModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.*;
import com.example.demo.service.TypeDocumentService;
import org.hibernate.boot.jaxb.SourceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.PriorityQueue;
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
    @Autowired
    TagRepository tagRepository;
    @Autowired
    LogRepository logRepository;

    final long time = 1522627200000L;

    public void clearDB() {
        notificationRepository.deleteAll();
        logRepository.deleteAll();
        bookingRepository.deleteAll();
        documentRepository.deleteAll();
        authorRepository.deleteAll();
        tagRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();
    }

    public void addUsers() {
        //БЕРИ ОТСЮДА ЮЗЕРОВ, ВТОРОЙ АТРИБУТ, ЭТО КТО ДОБАВЛЯЕТ
        Integer adminId = userController.addAdmin(new UserModel(-1, "Artyom", "Yu", "Via Margutta, 3", "30001", "admin", "art", "123"));

        userController.addUser(new UserModel(-1, "Sergey", "Afonso", "Via Margutta, 3", "30001", "Professor", "ser", "123"), adminId);

        userController.addUser(new UserModel(-1, "Nadia", "Teixeira", "Via Sacra, 13", " 30002", "Professor", "nad", "123"), adminId);

        userController.addUser(new UserModel(-1, "Elvira", "Espindola", "Via del Corso, 22", "30003", "Professor", "elv", "123"), adminId);

        userController.addUser(new UserModel(-1, "Andrey", "Velo", "Avenida Mazatlan 250", "30004", "Student", "and", "123"), adminId);

        userController.addUser(new UserModel(-1, "Veronika", "Rama", "Stret Atocha, 27", "30005", "VP", "ver", "123"), adminId);

        userController.addUser(new UserModel(-1, "Eugenia", "Rama", "Stret Atocha, 27", "30005", "Priv1", "eug", "123"), adminId);

        userController.addUser(new UserModel(-1, "Luie", "Ramos", "Stret Atocha, 27", "30005", "Priv2", "lui", "123"), adminId);

        userController.addUser(new UserModel(-1, "Ramon", "Valdez", "Stret Atocha, 27", "30005", "Priv3", "ram", "123"), adminId);
    }

    public void addDocuments(Integer librarianId) {
        //БЕРИ ОТСЮДА ДОКУМЕНТЫ, ВТОРОЙ АТРИБУТ, ЭТО КТО ДОБАВЛЯЕТ
        Set<Author> authors0 = new HashSet<>();
        authors0.add(new Author("Thomas H", "Cormen"));
        authors0.add(new Author("Charles E", "Leiserson"));
        authors0.add(new Author("Ronald L", "Rivest"));
        authors0.add(new Author("Clifford", "Stein"));

        Set<Tag> tags0 = new HashSet<Tag>();
        tags0.add(new Tag("Algorithms"));
        tags0.add(new Tag("Data Structures"));
        tags0.add(new Tag("Complexity"));
        tags0.add(new Tag("Computational Theory"));

        documentController.addDocument(new DocumentModel(1, "Introduction to Algorithms", authors0, 5000, 3, tags0, "MIT Press", 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")), librarianId);

        Set<Author> authors1 = new HashSet<>();
        authors1.add(new Author("Niklaus", "Wirth"));


        Set<Tag> tags1 = new HashSet<Tag>();
        tags1.add(new Tag("Algorithms"));
        tags1.add(new Tag("Data Structures"));
        tags1.add(new Tag("Search Algorithms"));
        tags1.add(new Tag("Pascal"));

        documentController.addDocument(new DocumentModel(1, "Algorithms + Data Structures = Programs", authors1, 5000, 3, tags1, "Prentice Hall PTR", 1, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")), librarianId);

        Set<Author> authors2 = new HashSet<>();
        authors2.add(new Author("Donald E.", "Knuth"));

        Set<Tag> tags2 = new HashSet<Tag>();
        tags2.add(new Tag("Algorithms"));
        tags2.add(new Tag("Combinatorial Algorithms"));
        tags2.add(new Tag("Recursion"));

        documentController.addDocument(new DocumentModel(1, "The Art of Computer Programming", authors2, 5000, 3, tags2, "Addison Wesley Longman Publishing Co., Inc.", 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")), librarianId);
    }

    @Test
    public void test1() {
        clearDB();
        addUsers();
        addDocuments(userRepository.findByUsername("ram").getId());

        clearDB();
    }

}