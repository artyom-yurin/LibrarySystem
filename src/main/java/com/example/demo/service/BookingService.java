package com.example.demo.service;

import com.example.demo.entity.Booking;
import com.example.demo.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    public void save(Booking booking)
    {
        bookingRepository.save(booking);
    }
    public List<Booking> getAllBackRequest()
    {
        return bookingRepository.findAllByHasBackRequestIsTrue();
    }
    public List<Booking> getAllBooking(Integer id)
    {
        return bookingRepository.findAllByUserIdAndHasBackRequestIsFalse(2);
    }
}
