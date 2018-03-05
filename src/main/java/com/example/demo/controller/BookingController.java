package com.example.demo.controller;


import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.stream.Collectors;

@RestController
public class BookingController {
    private BookingService bookingService;

    BookingController(BookingRepository bookingRepository)
    {
        bookingService = new BookingService(bookingRepository);
    }

    @GetMapping("/booking/find")
    public Iterable<Booking> findBookingByUserId(@RequestParam(name = "id", defaultValue = "-1") Integer id)
    {
        return bookingService.findAll()
                .stream()
                .filter(booking -> booking.getUser().getId().equals(id))
                .filter(booking -> !booking.isHasBackRequest())
                .collect(Collectors.toList());
    }
}
