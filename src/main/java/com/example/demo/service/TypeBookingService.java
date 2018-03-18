package com.example.demo.service;

import com.example.demo.entity.booking.TypeBooking;
import com.example.demo.repository.TypeBookingRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeBookingService {
    private TypeBookingRepository typeBookingRepository;

    public TypeBookingService (TypeBookingRepository typeBookingRepository) {
        this.typeBookingRepository = typeBookingRepository;
    }

    public TypeBooking findByTypeName(String name){
        return this.typeBookingRepository.findByTypeName(name);
    }
}
