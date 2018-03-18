package com.example.demo.service;

import com.example.demo.entity.booking.Booking;
import com.example.demo.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findAll()
    {
        return bookingRepository.findAll();
    }

    public void removeBookingById(Integer id){ bookingRepository.removeBookingById(id);}

    public void save(Booking booking){ this.bookingRepository.save(booking);}

    public Booking getBookingById(Integer id){ return bookingRepository.getBookingById(id);}
}
