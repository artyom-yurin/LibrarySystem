package com.example.demo.controllers;

import com.example.demo.controller.BookingContr;
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

    final long time = 1522627200000L;

    public void clearDB()
    {
        notificationRepository.deleteAll();
        bookingRepository.deleteAll();
        documentRepository.deleteAll();
        authorRepository.deleteAll();
        publisherRepository.deleteAll();
        userRepository.deleteAll();
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
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");

        bookingController.requestDocumentById(d1.getId(), p1.getId(), 	1525305600000L);

        bookingController.requestDocumentById(d2.getId(), p1.getId(), 	1525305600000L);

        bookingController.returnDocumentById(d2.getId());

        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(p1.getId()).get(0).getId(), 1525305600000L);

        Booking booking = bookingController.findMyBooking(p1.getId()).get(0);

        assert(booking.getFine() == 0);
        assert(booking.getReturnDate().getTime() > time);

        clearDB();
    }

    @Test
    public void test2() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");

        bookingController.requestDocumentById(d1.getId(), p1.getId(), 1525305600000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(p1.getId()).get(0).getId(), 1525305600000L);
        bookingController.requestDocumentById(d2.getId(), p1.getId(), 1525305600000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(p1.getId()).get(1).getId(), 1525305600000L);

        bookingController.requestDocumentById(d1.getId(), s.getId(), 1525305600000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(s.getId()).get(0).getId(), 1525305600000L);
        bookingController.requestDocumentById(d2.getId(), s.getId(), 1525305600000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(s.getId()).get(1).getId(), 1525305600000L);

        bookingController.requestDocumentById(d1.getId(), v.getId(), 1525305600000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(v.getId()).get(0).getId(), 1525305600000L);
        bookingController.requestDocumentById(d2.getId(), v.getId(), 1525305600000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(v.getId()).get(1).getId(), 1525305600000L);

        Booking bookingP1 = bookingController.findMyBooking(p1.getId()).get(0);
        Booking bookingP2 = bookingController.findMyBooking(p1.getId()).get(1);

        Booking bookingS1 = bookingController.findMyBooking(s.getId()).get(0);
        Booking bookingS2 = bookingController.findMyBooking(s.getId()).get(1);

        Booking bookingV1 = bookingController.findMyBooking(v.getId()).get(0);
        Booking bookingV2 = bookingController.findMyBooking(v.getId()).get(1);

        assert(bookingP1.getFine() == 0);
        assert(bookingP1.getReturnDate().getTime() > time);
        assert(bookingP2.getFine() == 0);
        assert(bookingP2.getReturnDate().getTime() > time);

        assert(bookingS1.getFine() >= 700);
        assert(time - bookingS1.getReturnDate().getTime() == 604800000);
        assert(bookingS2.getFine() >= 1400);
        assert(time - bookingS2.getReturnDate().getTime() == 1209600000L);

        assert(bookingV1.getFine() >= 2100);
        assert(time - bookingV1.getReturnDate().getTime() == 1814400000L);
        assert(bookingV2.getFine() >= 1700);
        assert(time - bookingV2.getReturnDate().getTime() == 1814400000L);

        clearDB();
    }

    @Test
    public void test3() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");

        bookingController.requestDocumentById(d1.getId(), p1.getId(), 1521590400000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(p1.getId()).get(0).getId(), 1521590400000L);

        bookingController.requestDocumentById(d2.getId(), s.getId(), 1521590400000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(s.getId()).get(0).getId(), 1521590400000L);

        bookingController.requestDocumentById(d2.getId(), v.getId(), 1521590400000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(v.getId()).get(0).getId(), 1521590400000L);

        bookingController.renewBook(bookingController.findMyBooking(p1.getId()).get(0).getId());
        bookingController.renewBook(bookingController.findMyBooking(s.getId()).get(0).getId());
        bookingController.renewBook(bookingController.findMyBooking(v.getId()).get(0).getId());

        assert(bookingController.findMyBooking(p1.getId()).get(0).getReturnDate().getTime() >= 1525046400000L);
        assert(bookingController.findMyBooking(s.getId()).get(0).getReturnDate().getTime() >= 1523836800000L);
        assert(bookingController.findMyBooking(v.getId()).get(0).getReturnDate().getTime() >= 1523232000000L);

        clearDB();
    }

    @Test
    public void test4() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User p2 = userRepository.findByUsername("nad");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");

        bookingController.requestDocumentById(d1.getId(), p1.getId(), 1521590400000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(p1.getId()).get(0).getId(), 1521590400000L);

        bookingController.requestDocumentById(d2.getId(), s.getId(), 1521590400000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(s.getId()).get(0).getId(), 1521590400000L);

        bookingController.requestDocumentById(d2.getId(), v.getId(), 1521590400000L);
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(v.getId()).get(0).getId(), 1521590400000L);

        bookingController.requestDocumentById(d2.getId(), p2.getId(), 1521590400000L);
        bookingController.makeOutstandingRequest(bookingController.findMyBooking(p2.getId()).get(0).getId());

        bookingController.renewBook(bookingController.findMyBooking(p1.getId()).get(0).getId());
        bookingController.renewBook(bookingController.findMyBooking(s.getId()).get(0).getId());
        bookingController.renewBook(bookingController.findMyBooking(v.getId()).get(0).getId());

        assert(bookingController.findMyBooking(p1.getId()).get(0).getReturnDate().getTime() >= 1525046400000L);
        assert(bookingController.findMyBooking(s.getId()).get(0).getReturnDate().getTime() >= 1523491200000L);
        assert(bookingController.findMyBooking(v.getId()).get(0).getReturnDate().getTime() >= 1522886400000L);

        clearDB();
    }

    @Test
    public void test5() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d3 = documentRepository.findByTitle("Null References: The Billion Dollar Mistake");

        bookingController.requestDocumentById(d3.getId(), p1.getId(), System.currentTimeMillis());
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking(p1.getId()).get(0).getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), s.getId(), System.currentTimeMillis());
        bookingController.takeDocumentByBookingId(bookingController.findMyBooking((s.getId())).get(0).getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), v.getId(), System.currentTimeMillis());

        assert(bookingController.getQueueForBook(d3.getId()).size() == 1);
        assert(bookingController.getQueueForBook(d3.getId()).poll().getUser().getUsername().equals("ver"));

        clearDB();
    }

    @Test
    public void test6() {
        clearDB();
        addUsers();
        addDocuments();
        User p1 = userRepository.findByUsername("ser");
        User p2 = userRepository.findByUsername("nad");
        User p3 = userRepository.findByUsername("elv");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        Document d3 = documentRepository.findByTitle("Null References: The Billion Dollar Mistake");

        bookingController.requestDocumentById(d3.getId(), p1.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p2.getId(),  System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), s.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), v.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p3.getId(), System.currentTimeMillis());

        PriorityQueue<Booking> pq = bookingController.getQueueForBook(d3.getId());
        assert(pq.size() == 3);
        assert(pq.poll().getUser().getUsername().equals("and"));
        assert(pq.poll().getUser().getUsername().equals("ver"));
        assert(pq.poll().getUser().getUsername().equals("elv"));

        clearDB();
    }

    @Test
    public void test7() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User p2 = userRepository.findByUsername("nad");
        User p3 = userRepository.findByUsername("elv");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        Document d3 = documentRepository.findByTitle("Null References: The Billion Dollar Mistake");

        bookingController.requestDocumentById(d3.getId(), p1.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p2.getId(),  System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), s.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), v.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p3.getId(), System.currentTimeMillis());
        PriorityQueue<Booking> pq = bookingController.getQueueForBook(d3.getId());

        bookingController.makeOutstandingRequest(pq.poll().getId());

        pq = bookingController.getQueueForBook(d3.getId());

        assert(pq.size() == 1);
        assert ("Your queue position is cancelled".equals(notificationController.findMyNotifications(v.getId()).get(0).getMessage()));
        assert ("Your queue position is cancelled".equals(notificationController.findMyNotifications(p3.getId()).get(0).getMessage()));

        clearDB();
    }

    @Test
    public void test8() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User p2 = userRepository.findByUsername("nad");
        User p3 = userRepository.findByUsername("elv");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        Document d3 = documentRepository.findByTitle("Null References: The Billion Dollar Mistake");

        bookingController.requestDocumentById(d3.getId(), p1.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p2.getId(),  System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), s.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), v.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p3.getId(), System.currentTimeMillis());

        bookingController.closeBooking(bookingController.findMyBooking(p2.getId()).get(0).getId(), System.currentTimeMillis());

        PriorityQueue<Booking> pq = bookingController.getQueueForBook(d3.getId());

        assert (pq.size() == 2);
        assert ("available".equals(bookingController.findMyBooking(s.getId()).get(0).getTypeBooking().getTypeName()));
        assert ("Null References: The Billion Dollar Mistake is available for you".equals(notificationController.findMyNotifications(s.getId()).get(0).getMessage()));
        clearDB();
    }

    @Test
    public void test9() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User p2 = userRepository.findByUsername("nad");
        User p3 = userRepository.findByUsername("elv");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        Document d3 = documentRepository.findByTitle("Null References: The Billion Dollar Mistake");

        bookingController.requestDocumentById(d3.getId(), p1.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p2.getId(),  System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), s.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), v.getId(), System.currentTimeMillis());

        bookingController.requestDocumentById(d3.getId(), p3.getId(), System.currentTimeMillis());

        Integer p1Index = bookingController.findMyBooking(p1.getId()).get(0).getId();

        bookingController.takeDocumentByBookingId(p1Index, 1522627200000L);
        bookingController.renewBook(p1Index);

        assert (1525046400000L == bookingRepository.findOne(p1Index).getReturnDate().getTime());

        clearDB();
    }

    @Test
    public void test10() {
        clearDB();
        addUsers();
        addDocuments();

        User p1 = userRepository.findByUsername("ser");
        User p2 = userRepository.findByUsername("nad");
        User p3 = userRepository.findByUsername("elv");
        User s = userRepository.findByUsername("and");
        User v = userRepository.findByUsername("ver");

        Document d1 = documentRepository.findByTitle("Introduction to Algorithms");
        Document d2 = documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Software");
        Document d3 = documentRepository.findByTitle("Null References: The Billion Dollar Mistake");

        bookingController.requestDocumentById(d1.getId(), p1.getId(), 1522022400000L);
        bookingController.requestDocumentById(d1.getId(), v.getId(), 1522022400000L);
        Integer p1Index = bookingController.findMyBooking(p1.getId()).get(0).getId();

        bookingController.takeDocumentByBookingId(p1Index, 1522022400000L);
        bookingController.renewBook(p1Index);

        Integer vIndex = bookingController.findMyBooking(v.getId()).get(0).getId();

        bookingController.takeDocumentByBookingId(vIndex, 1522022400000L);
        bookingController.renewBook(vIndex);

        try {
            bookingController.renewBook(p1Index);
            assert(false);
        }
        catch (Exception ignored)
        {}


        bookingController.renewBook(vIndex);
        assert (1525651200000L == bookingRepository.findOne(p1Index).getReturnDate().getTime());
        assert (1523836800000L == bookingRepository.findOne(vIndex).getReturnDate().getTime());

        clearDB();
    }
}