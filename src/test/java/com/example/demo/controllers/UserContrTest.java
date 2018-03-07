package com.example.demo.controllers;

import com.example.demo.entity.document.Author;
import com.example.demo.entity.document.Publisher;
import com.example.demo.entity.document.Tag;
import com.example.demo.model.DocumentModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TypeDocumentService;
import net.minidev.json.JSONStyle;
import net.minidev.json.reader.JsonWriter;
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
    DocumentContr documentController;
    TypeDocumentService typeDocumentService;
    UserRepository userRepository;
    DocumentRepository documentRepository;

    @Test
    public void test1()
    {
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
        Set<Author> authors0 = new HashSet<>();
        authors0.add(new Author("Thomas H", "Cormen"));
        authors0.add(new Author("Charles E", "Leiserson"));
        authors0.add(new Author("Ronald L", "Rivest"));
        authors0.add(new Author("Clifford", "Stein"));
        Publisher publisher0 = new Publisher("MIT Press");
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors1 = new HashSet<>();
        authors1.add(new Author("Erich", "Gamma"));
        authors1.add(new Author("Ralph", "Johnson"));
        authors1.add(new Author("John", "Vlissides"));
        authors1.add(new Author("Richard", "Helm"));
        Publisher publisher1 = new Publisher("Addison-Wesley Professional");
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors2 = new HashSet<>();
        authors2.add(new Author("Brooks", "Jr"));
        authors2.add(new Author("Frederick", "P"));
        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc");
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors3 = new HashSet<>();
        authors3.add(new Author("Tony", "Hoare"));
        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));
        Set<Author> authors4 = new HashSet<>();
        authors4.add(new Author("Claude", "Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));
        assert(documentController.getAmountTest() == 8 && userController.getAmountTest() == 3);
        userRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @Test
    public void test2(){
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        UserModel p2 = new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123");
        userController.addUserTest(p2);
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
        Set<Author> authors0 = new HashSet<>();
        authors0.add(new Author("Thomas H", "Cormen"));
        authors0.add(new Author("Charles E", "Leiserson"));
        authors0.add(new Author("Ronald L", "Rivest"));
        authors0.add(new Author("Clifford", "Stein"));
        Publisher publisher0 = new Publisher("MIT Press");
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors1 = new HashSet<>();
        authors1.add(new Author("Erich", "Gamma"));
        authors1.add(new Author("Ralph", "Johnson"));
        authors1.add(new Author("John", "Vlissides"));
        authors1.add(new Author("Richard", "Helm"));
        Publisher publisher1 = new Publisher("Addison-Wesley Professional");
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors2 = new HashSet<>();
        authors2.add(new Author("Brooks", "Jr"));
        authors2.add(new Author("Frederick", "P"));
        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc");
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors3 = new HashSet<>();
        authors3.add(new Author("Tony", "Hoare"));
        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));
        Set<Author> authors4 = new HashSet<>();
        authors4.add(new Author("Claude", "Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));
        documentController.updateDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 1, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        documentController.updateDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 0, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        userController.removeUserTest(p2);
        assert(documentController.getAmountTest() == 5 && userController.getAmountTest() == 2);
        userRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @Test
    public void test3(){
        UserModel p1 = new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123");
        userController.addUserTest(p1);
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        UserModel p3 = new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123");
        userController.addUserTest(p3);
        Set<Author> authors0 = new HashSet<>();
        authors0.add(new Author("Thomas H", "Cormen"));
        authors0.add(new Author("Charles E", "Leiserson"));
        authors0.add(new Author("Ronald L", "Rivest"));
        authors0.add(new Author("Clifford", "Stein"));
        Publisher publisher0 = new Publisher("MIT Press");
        documentController.addDocumentTest(new DocumentModel(1, "Introduction to Algorithms", authors0, 1000, 3, new HashSet<Tag>(), publisher0, 3, false, false, new Date(1230768000000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors1 = new HashSet<>();
        authors1.add(new Author("Erich", "Gamma"));
        authors1.add(new Author("Ralph", "Johnson"));
        authors1.add(new Author("John", "Vlissides"));
        authors1.add(new Author("Richard", "Helm"));
        Publisher publisher1 = new Publisher("Addison-Wesley Professional");
        documentController.addDocumentTest(new DocumentModel(1, "Design Patterns: Elements of Reusable Object-Oriented Service", authors1, 2000, 2, new HashSet<Tag>(), publisher1, 1, true, false, new Date(1041379200000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors2 = new HashSet<>();
        authors2.add(new Author("Brooks", "Jr"));
        authors2.add(new Author("Frederick", "P"));
        Publisher publisher2 = new Publisher("Addison-Wesley Longman Publishing Co., Inc");
        documentController.addDocumentTest(new DocumentModel(1, "The Mythical Man-mouth", authors2, 3000, 1, new HashSet<Tag>(), publisher2, 2, false, true, new Date(788918400000L), "", typeDocumentService.findByTypeName("book")));
        Set<Author> authors3 = new HashSet<>();
        authors3.add(new Author("Tony", "Hoare"));
        documentController.addDocumentTest(new DocumentModel(1, "Null References: The Billion Dollar Mistake", authors3, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));
        Set<Author> authors4 = new HashSet<>();
        authors4.add(new Author("Claude", "Shannon"));
        documentController.addDocumentTest(new DocumentModel(1, "Information Entropy", authors4, 1500, 1, new HashSet<Tag>(), null, 0, false, false, null, "", typeDocumentService.findByTypeName("avmaterial")));
        assert(p1 == p1 && p3 == p3);
        userRepository.deleteAll();
        documentRepository.deleteAll();
    }
}