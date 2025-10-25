package com.example.salon_management.service.impl;

import com.example.salon_management.entity.Booking;
import com.example.salon_management.repository.BookingRepository;
import com.example.salon_management.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repo;

    public BookingServiceImpl(BookingRepository repo) {
        this.repo = repo;
    }

    @Override public Booking save(Booking b) { return repo.save(b); }
    @Override public Booking findById(Long id) { return repo.findById(id).orElse(null); }
    @Override public Booking findByCode(String code) { return repo.findByCode(code).orElse(null); }
    @Override public List<Booking> findAll() { return repo.findAll(); }
    @Override public void delete(Long id) { repo.deleteById(id); }
}
