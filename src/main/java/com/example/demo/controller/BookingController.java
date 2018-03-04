package com.example.demo.controller;


import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {
    private BookingService bookingService;

    BookingController(BookingRepository bookingRepository)
    {
        bookingService = new BookingService(bookingRepository);
    }
}
