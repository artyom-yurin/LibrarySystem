package com.example.demo.controller;


import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Method;

@Controller
public class BookingController {
    private BookingService bookingService;

    BookingController(BookingRepository bookingRepository)
    {
        bookingService = new BookingService(bookingRepository);
    }
}
