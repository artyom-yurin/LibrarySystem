package com.example.demo.controller;

import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    final UserRepository userRepository;

    public IndexController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/catalog")
    public ModelAndView catalog() { return new ModelAndView("catalog"); }

    @GetMapping("/{userId}/control")
    public ModelAndView control() {
        return new ModelAndView("control");
    }

    @GetMapping("/{userId}/myBooks")
    public ModelAndView myBooks() {
        return new ModelAndView("myBooks");
    }
}
