package com.example.demo.controller;

import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BookingRepository bookingRepository;

    public IndexController(UserRepository userRepository, RoleRepository roleRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/catalog")
    public ModelAndView catalog() { return new ModelAndView("catalog"); }

    @GetMapping("/control")
    public ModelAndView control() {
        return new ModelAndView("control");
    }

    @GetMapping("/myBooks")
    public ModelAndView myBooks() {
        return new ModelAndView("myBooks");
    }
}
