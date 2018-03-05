package com.example.demo.controller;

import com.example.demo.exception.AlreadyLoginException;
import com.example.demo.repository.BookingRepository;
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
    private final BookingRepository bookingRepository;
    private final TagRepository tagRepository;

    public IndexController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           TagRepository tagRepository,
                          BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tagRepository = tagRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/")
    public ModelAndView login(HttpServletRequest request) {
        ParserToken token = TokenAuthenticationService.getAuthentication(request);
        if (token == null)
        {
            return new ModelAndView("login");
        }
        throw new AlreadyLoginException();
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
