package com.example.demo.controllers;

import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Tag;
import com.example.demo.entity.user.User;
import com.example.demo.exception.AccessDeniedException;
import com.example.demo.model.DocumentModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.*;
import com.example.demo.service.TypeDocumentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
        boolean check = false;
        try {
            userController.addAdmin(new UserModel(-1, "Artyom", "Yu", "Via Margutta, 3", "30001", "admin", "art", "123"));
        } catch (AccessDeniedException exception) {
            check = true;
        }
        assert check;
        clearDB();
    }

    @Test
    public void test2() {
        clearDB();
        addUsers();
        List<User> librarians = userController.getLibrarians();
        assert librarians.size() == 3;
        clearDB();
    }

    @Test
    public void test3() {
        clearDB();
        addUsers();
        boolean check = false;
        try { addDocuments(userRepository.findByUsername("eug").getId()); }
        catch (AccessDeniedException exception) {
            check = true;
        }
        assert check;
        clearDB();
    }

    @Test
    public void test4() {
        clearDB();
        addUsers();
        addDocuments(userRepository.findByUsername("lui").getId());
        int count = 0;
        for (User user: userController.getUsers()) {
            count++;
        }
        int count2 = 0;
        for (Document document: documentController.getDocuments()) {
            count2++;
        }
        assert count == 5;
        assert count2 == 3;
        clearDB();
    }

    @Test
    public void test5(){
        clearDB();
        addUsers();
        addDocuments(userRepository.findByUsername("lui").getId());
        documentController.removeCopy(documentRepository.findByTitle("Introduction to Algorithms").getId(), userRepository.findByUsername("ram").getId());
        assert documentRepository.findByTitle("Introduction to Algorithms").getCount() == 2;
        clearDB();
    }

    @Test
    public void test6(){
        clearDB();
        addUsers();
        addDocuments(userRepository.findByUsername("lui").getId());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("ser").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("nad").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("elv").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("and").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("ver").getId(), System.currentTimeMillis());
        boolean check = false;
        try { bookingController.makeOutstandingRequest(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("eug").getId(), System.currentTimeMillis());}
        catch (AccessDeniedException exception) { check = true; }
        assert check;
        clearDB();
    }

    @Test
    public void test7(){
        clearDB();
        addUsers();
        addDocuments(userRepository.findByUsername("lui").getId());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("ser").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("nad").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("elv").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("and").getId(), System.currentTimeMillis());
        bookingController.requestDocumentById(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("ver").getId(), System.currentTimeMillis());
        bookingController.makeOutstandingRequest(documentRepository.findByTitle("The Art of Computer Programming").getId(), userRepository.findByUsername("ram").getId(), System.currentTimeMillis());
        assert notificationController.findMyNotifications(userRepository.findByUsername("ser").getId()).size() == 1;
        assert notificationController.findMyNotifications(userRepository.findByUsername("nad").getId()).size() == 1;
        assert notificationController.findMyNotifications(userRepository.findByUsername("elv").getId()).size() == 1;
        assert notificationController.findMyNotifications(userRepository.findByUsername("and").getId()).size() == 1;
        assert notificationController.findMyNotifications(userRepository.findByUsername("ver").getId()).size() == 1;
        clearDB();
    }

    @Test
    public void test8() throws AccessDeniedException{
        clearDB();
        //НЕ ЕБУ КАК ДЕЛАТЬ ЛОООГИ
        clearDB();
    }

    @Test
    public void test9() throws AccessDeniedException{
        clearDB();
        //НЕ ЕБУ КАК ДЕЛАТЬ ЛОООГИ
        clearDB();
    }
}