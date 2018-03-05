package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking findById(Integer id)
    {
        Booking book = null;
        try
        {
            book = bookingRepository.findById(id);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return book;
    }
}
