package com.example.demo.controllers;

import com.example.demo.model.UserModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class UserContrTest {
    @Autowired
    UserContr userController;

    @Test
    public void test1()
    {
        userController.addUserTest(new UserModel(1,"Sergey", "Afonso", "Via Margutta, 3", "30001", "Faculty", "ser", "123"));
        userController.addUserTest(new UserModel(1,"Nadia", "Teixeira", "Via Sacra, 13", "30002", "Patron", "nad", "123"));
        userController.addUserTest(new UserModel(1,"Elvira", "Espindola", "Via del Corso, 22", "30003", "Patron", "elv", "123"));
        System.out.println("Test 1 success");
    }
}