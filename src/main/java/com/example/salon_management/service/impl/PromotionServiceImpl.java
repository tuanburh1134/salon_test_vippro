package com.example.salon_management.service.impl;

import com.example.salon_management.entity.Promotion;
import com.example.salon_management.repository.PromotionRepository;
import com.example.salon_management.service.PromotionService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository repo;

    public PromotionServiceImpl(PromotionRepository repo) {
        this.repo = repo;
    }

    @Override
    public Promotion save(Promotion p) {
        Assert.notNull(p.getPercent(), "Percent required");
        if (p.getPercent() < 1 || p.getPercent() > 100) {
            throw new IllegalArgumentException("Phần trăm khuyến mãi phải từ 1 đến 100");
        }
        return repo.save(p);
    }

    @Override public List<Promotion> findAll() { return repo.findAll(); }
    @Override public Promotion findById(Long id) { return repo.findById(id).orElse(null); }
    @Override public void delete(Long id) { repo.deleteById(id); }
}
