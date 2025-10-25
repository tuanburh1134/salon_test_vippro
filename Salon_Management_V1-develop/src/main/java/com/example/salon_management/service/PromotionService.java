package com.example.salon_management.service;

import com.example.salon_management.entity.Promotion;

import java.util.List;

public interface PromotionService {
    Promotion save(Promotion p);
    List<Promotion> findAll();
    Promotion findById(Long id);
    void delete(Long id);
}
