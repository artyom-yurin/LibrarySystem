package com.example.demo.controllers;

import com.example.demo.controller.BookingContr;
import com.example.demo.entity.booking.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SystemContr {

    @Autowired
    private BookingContr bookingController;

    private static final long WEEK_AFTER_END = 604800000L;

    public void systemUpdate(long currentTime) {
        Long systemTime = currentTime;
        for (Booking booking : bookingController.findActiveBookings()) {
            if (booking.getReturnDate().getTime() < systemTime) {
                bookingController.applyMeasures(booking, currentTime);
            } else if (booking.getReturnDate().getTime() - systemTime < WEEK_AFTER_END) {
                //TODO: NOTIFICATION ABOUT WEEK AFTER END
            }
        }
    }
}
