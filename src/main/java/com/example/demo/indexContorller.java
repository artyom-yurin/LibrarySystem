package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class indexContorller {

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
