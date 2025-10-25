package com.example.salon_management.service;

import com.example.salon_management.dto.CustomerForm;
import com.example.salon_management.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Page<Customer> search(String keyword, Pageable pageable);
    Customer get(Long id);
    void create(CustomerForm form);
    void update(Long id, CustomerForm form);
    void delete(Long id);

    // thêm để PaymentController/… gọi dropdown
    List<Customer> findAll();
}
