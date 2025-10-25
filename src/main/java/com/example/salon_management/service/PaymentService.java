package com.example.salon_management.service;

import com.example.salon_management.dto.PaymentForm;
import com.example.salon_management.entity.Payment;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    Payment create(PaymentForm form);
    List<Payment> search(LocalDateTime from, LocalDateTime to);
    List<Payment> findAll();
}
