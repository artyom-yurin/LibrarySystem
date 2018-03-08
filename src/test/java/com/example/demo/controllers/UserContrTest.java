package com.example.demo.controllers;

import com.example.demo.entity.Booking;
import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Document;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.Tag;
import com.example.demo.entity.user.User;
import com.example.demo.model.DocumentModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.*;
import com.example.demo.service.TypeDocumentService;
import net.minidev.json.JSONStyle;
import net.minidev.json.reader.JsonWriter;
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
public class UserContrTest {
    @Autowired
    UserContr userController;
    @Autowired
    DocumentContr documentController;
    @Autowired
    BookingContr bookingController;
    @Autowired
    TypeDocumentService typeDocumentService;
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
    public void clearDB()
    {
        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void test1()
    {
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
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
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));

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
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors2 = new HashSet<>();

        tempAuthor = new Author("Brooks", "Jr");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("Jr"));

        tempAuthor = new Author("Frederick", "P");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("P"));

        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        publisherRepository.save(publisher2);
        publisher2 = publisherRepository.findByPublisherName("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors3 = new HashSet<>();
        tempAuthor = new Author("Tony", "Hoare");
        authorRepository.save(tempAuthor);
        authors3.add(authorRepository.findByLastName("Hoare"));

        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        Set<Author> authors4 = new HashSet<>();
        tempAuthor = new Author("Claude", "Shannon");
        authorRepository.save(tempAuthor);
        authors4.add(authorRepository.findByLastName("Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        assert(documentController.getAmountTest() == 8 && userController.getAmountTest() == 3);

        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void test2(){
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
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
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));

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
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors2 = new HashSet<>();

        tempAuthor = new Author("Brooks", "Jr");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("Jr"));

        tempAuthor = new Author("Frederick", "P");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("P"));

        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        publisherRepository.save(publisher2);
        publisher2 = publisherRepository.findByPublisherName("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors3 = new HashSet<>();
        tempAuthor = new Author("Tony", "Hoare");
        authorRepository.save(tempAuthor);
        authors3.add(authorRepository.findByLastName("Hoare"));

        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        Set<Author> authors4 = new HashSet<>();
        tempAuthor = new Author("Claude", "Shannon");
        authorRepository.save(tempAuthor);
        authors4.add(authorRepository.findByLastName("Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));


        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("Introduction to Algorithms").getId(), "Introduction to Algorithms", authors0, 1000, 1, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("The Mythical Man-mouth").getId(), "The Mythical Man-mouth", authors2, 3000, 0, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        userController.removeUserTest(userRepository.findByUsername("nad").getId());
        assert(documentController.getAmountTest() == 5 && userController.getAmountTest() == 2);
        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void test3(){
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
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
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));

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
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors2 = new HashSet<>();

        tempAuthor = new Author("Brooks", "Jr");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("Jr"));

        tempAuthor = new Author("Frederick", "P");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("P"));

        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        publisherRepository.save(publisher2);
        publisher2 = publisherRepository.findByPublisherName("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors3 = new HashSet<>();
        tempAuthor = new Author("Tony", "Hoare");
        authorRepository.save(tempAuthor);
        authors3.add(authorRepository.findByLastName("Hoare"));

        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        Set<Author> authors4 = new HashSet<>();
        tempAuthor = new Author("Claude", "Shannon");
        authorRepository.save(tempAuthor);
        authors4.add(authorRepository.findByLastName("Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        User p1 = userRepository.findByUsername("ser");
        User p2 = userRepository.findByUsername("elv");

        assert("Sergey".equals(p1.getName()) && "Elvira".equals(p2.getName()));
        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void test4(){
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
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
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));

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
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors2 = new HashSet<>();

        tempAuthor = new Author("Brooks", "Jr");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("Jr"));

        tempAuthor = new Author("Frederick", "P");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("P"));

        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        publisherRepository.save(publisher2);
        publisher2 = publisherRepository.findByPublisherName("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors3 = new HashSet<>();
        tempAuthor = new Author("Tony", "Hoare");
        authorRepository.save(tempAuthor);
        authors3.add(authorRepository.findByLastName("Hoare"));

        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        Set<Author> authors4 = new HashSet<>();
        tempAuthor = new Author("Claude", "Shannon");
        authorRepository.save(tempAuthor);
        authors4.add(authorRepository.findByLastName("Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));


        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("Introduction to Algorithms").getId(), "Introduction to Algorithms", authors0, 1000, 1, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("The Mythical Man-mouth").getId(), "The Mythical Man-mouth", authors2, 3000, 0, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        userController.removeUserTest(userRepository.findByUsername("nad").getId());


        assert(userRepository.findByUsername("nad") == null);
        assert("Elvira".equals(userRepository.findByUsername("elv").getName()));
        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();

    }

    @Test
    public void test5(){
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
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
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));

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
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors2 = new HashSet<>();

        tempAuthor = new Author("Brooks", "Jr");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("Jr"));

        tempAuthor = new Author("Frederick", "P");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("P"));

        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        publisherRepository.save(publisher2);
        publisher2 = publisherRepository.findByPublisherName("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors3 = new HashSet<>();
        tempAuthor = new Author("Tony", "Hoare");
        authorRepository.save(tempAuthor);
        authors3.add(authorRepository.findByLastName("Hoare"));

        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        Set<Author> authors4 = new HashSet<>();
        tempAuthor = new Author("Claude", "Shannon");
        authorRepository.save(tempAuthor);
        authors4.add(authorRepository.findByLastName("Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));


        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("Introduction to Algorithms").getId(), "Introduction to Algorithms", authors0, 1000, 1, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("The Mythical Man-mouth").getId(), "The Mythical Man-mouth", authors2, 3000, 0, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        userController.removeUserTest(userRepository.findByUsername("nad").getId());

        try
        {
            bookingController.requestDocumentByIdTest(documentRepository.findByTitle("Introduction to Algorithms").getId(), userRepository.findByUsername("nad"));
            assert(false);
        }
        catch (Exception ignore)
        {}
        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void test6(){
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
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
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));

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
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors2 = new HashSet<>();

        tempAuthor = new Author("Brooks", "Jr");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("Jr"));

        tempAuthor = new Author("Frederick", "P");
        authorRepository.save(tempAuthor);
        authors2.add(authorRepository.findByLastName("P"));

        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        publisherRepository.save(publisher2);
        publisher2 = publisherRepository.findByPublisherName("Addison-Wesley Longman Publishing Co., Inc".toLowerCase());
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));

        Set<Author> authors3 = new HashSet<>();
        tempAuthor = new Author("Tony", "Hoare");
        authorRepository.save(tempAuthor);
        authors3.add(authorRepository.findByLastName("Hoare"));

        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));

        Set<Author> authors4 = new HashSet<>();
        tempAuthor = new Author("Claude", "Shannon");
        authorRepository.save(tempAuthor);
        authors4.add(authorRepository.findByLastName("Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));


        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("Introduction to Algorithms").getId(), "Introduction to Algorithms", authors0, 1000, 1, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        documentController.updateDocumentTest(new DocumentModel(documentRepository.findByTitle("The Mythical Man-mouth").getId(), "The Mythical Man-mouth", authors2, 3000, 0, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        userController.removeUserTest(userRepository.findByUsername("nad").getId());

        bookingController.requestDocumentByIdTest(documentRepository.findByTitle("Introduction to Algorithms").getId(), userRepository.findByUsername("ser"));

        try
        {
            bookingController.requestDocumentByIdTest(documentRepository.findByTitle("Introduction to Algorithms").getId(), userRepository.findByUsername("elv"));
            assert (false);
        }
        catch (Exception ignore){}

        bookingController.requestDocumentByIdTest(documentRepository.findByTitle("Design Patterns: Elements of Reusable Object-Oriented Service").getId(), userRepository.findByUsername("ser"));

        assert(bookingController.findBookingByUserIdTest(userRepository.findByUsername("ser").getId()).size() == 2);
        assert(bookingController.findBookingByUserIdTest(userRepository.findByUsername("elv").getId()).size() == 0);

        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }
}