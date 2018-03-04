package com.example.demo.controller;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingRepository bookingRepository)
    {
        bookingService = new BookingService(bookingRepository);
    }

    /*@PostMapping("booking/take")
    public void UserTakeBook(@RequestBody BookingModel bookingModel)
    {
    }*/
    @PutMapping("booking/return")
    public void UserWantReturnBook(@RequestBody Booking booking)
    {
        booking.setHasBackRequest(true);
        bookingService.save(booking);
    }
    @PutMapping("booking/request/close")
    public void CloseRequest(@RequestBody Booking booking)
    {
        booking.setClose(true);
        booking.setHasBackRequest(false);
        bookingService.save(booking);
    }
    @PutMapping("booking/request/cancel")
    public void CancelRequest(@RequestBody Booking booking)
    {
        booking.setHasBackRequest(false);
        bookingService.save(booking);
    }

    @GetMapping("booking/request/get_all")
    public List<Booking> GetAllBackRequest()
    {
        return bookingService.getAllBackRequest();
    }
    @GetMapping("booking/get_all")
    public List<Booking> GetAllBooking()
    {
        return bookingService.getAllBooking(2);
    }
}
