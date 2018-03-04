package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer>{
    Booking findById(Integer id);

    List<Booking> findAllByHasBackRequestIsTrue();

    List<Booking> findAllByUserIdAndHasBackRequestIsFalse(Integer id);
}

