package com.example.demo.repository;

import com.example.demo.entity.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking getBookingById(Integer id);
    void removeBookingById(Integer id);
}
