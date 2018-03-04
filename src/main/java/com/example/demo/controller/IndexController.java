package com.example.demo.controller;

import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.example.security.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TagRepository tagRepository;

    public IndexController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/")
    public ModelAndView login(HttpServletRequest request) {
        return new ModelAndView("login");
    }

    @GetMapping("/catalog")
    public ModelAndView catalog() {
        return new ModelAndView("catalog");
    }

    @GetMapping("/control")
    public ModelAndView control() {
        return new ModelAndView("control");
    }

    @GetMapping("/myBooks")
    public ModelAndView myBooks() {
        return new ModelAndView("myBooks");
    }
}
