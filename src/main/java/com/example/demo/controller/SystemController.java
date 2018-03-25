package com.example.demo.controller;

import com.example.demo.entity.booking.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SystemController {

    @Autowired
    private BookingController bookingController;

    @GetMapping("/system/time")
    public long getSystemTime()
    {
        return System.currentTimeMillis();
    }

    @PutMapping("/system/update")
    public void systemUpdate()
    {
        Long systemTime = System.currentTimeMillis();
        for(Booking booking : bookingController.findActiveBookings())
        {
            if (booking.getReturnDate().getTime() < systemTime)
            {
                bookingController.applyMeasures(booking);
            }
        }
    }
}
