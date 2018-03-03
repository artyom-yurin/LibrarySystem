package com.example.demo.controller;

import com.example.demo.entity.user.User;
import com.example.demo.model.LoginModel;
import com.example.demo.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ApiController {

    final UserRepository userRepository;

    public ApiController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/api/login")
    public ModelAndView login(@RequestBody LoginModel loginModel)
    {
        if ("login".equals(loginModel.getUsername()) && "password".equals(loginModel.getPassword()))
        {
            return new ModelAndView("catalog");
        }
        return null;
    }

    @GetMapping("/api/users")
    public Iterable<User> getUsers()
    {
        return userRepository.findAll();
    }
}
