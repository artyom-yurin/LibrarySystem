package com.example.demo.controllers;

import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.Tag;
import com.example.demo.entity.user.User;
import com.example.demo.model.DocumentModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.*;
import com.example.demo.service.TypeDocumentService;
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
    public void clearDB() {
        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void test1() {
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


        userRepository.deleteAll();
        documentRepository.deleteAll();
        publisherRepository.deleteAll();
        authorRepository.deleteAll();
        bookingRepository.deleteAll();
    }
}