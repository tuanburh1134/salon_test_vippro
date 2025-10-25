package com.example.salon_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.salon_management.dto.ServiceItemRequest;
import com.example.salon_management.entity.ServiceItem;

import java.util.Optional;

public interface ServiceItemService {
    Page<ServiceItem> search(String keyword, String type, Pageable pageable);
    ServiceItem create(ServiceItemRequest req);
    ServiceItem update(Long id, ServiceItemRequest req);
    void delete(Long id);
    Optional<ServiceItem> findById(Long id);
}

