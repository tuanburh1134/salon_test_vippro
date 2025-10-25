package com.example.salon_management.service;

import com.example.salon_management.entity.Booking;

import java.util.List;

public interface BookingService {
    Booking save(Booking b);
    Booking findById(Long id);
    Booking findByCode(String code);
    List<Booking> findAll();
    void delete(Long id);
}
