package com.example.salon_management.service;

import com.example.salon_management.dto.ProductUsageForm;
import com.example.salon_management.entity.ProductUsage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductUsageService {

    // khớp với ProductUsageController.list(...)
    Page<ProductUsage> search(String keyword, Pageable pageable);

    // khớp với Controller: get, create, update, delete
    ProductUsage get(Long id);
    void create(ProductUsageForm form);
    void update(Long id, ProductUsageForm form);
    void delete(Long id);

    // dùng khi cần đổ dropdown
    List<ProductUsage> findAll();
}
