package com.example.salon_management.repository;

import com.example.salon_management.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPaidAtBetween(LocalDateTime start, LocalDateTime end);
}
