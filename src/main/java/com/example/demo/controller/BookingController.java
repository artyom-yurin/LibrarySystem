package com.example.demo.controller;

import com.example.demo.entity.Booking;
import com.example.demo.entity.user.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Book;

@Controller
public class BookingController {

    private BookingService bookingService;
    private UserService userService;

    public BookingController(UserRepository userRepository, BookingRepository bookingRepository) {
        this.bookingService = new BookingService(bookingRepository);
        this.userService = new UserService(userRepository);
    }
}
