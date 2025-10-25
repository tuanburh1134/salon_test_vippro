package com.example.salon_management.service.impl;

import com.example.salon_management.dto.ProductUsageForm;
import com.example.salon_management.entity.ProductUsage;
import com.example.salon_management.repository.ProductUsageRepository;
import com.example.salon_management.service.ProductUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductUsageServiceImpl implements ProductUsageService {

    private final ProductUsageRepository repo;

    @Override
    public Page<ProductUsage> search(String keyword, Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public ProductUsage get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ProductUsage id=" + id));
    }

    @Override
    public void create(ProductUsageForm f) {
        ProductUsage u = new ProductUsage();
        apply(u, f);
        repo.save(u);
    }

    @Override
    public void update(Long id, ProductUsageForm f) {
        ProductUsage u = get(id);
        apply(u, f);
        repo.save(u);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<ProductUsage> findAll() {
        return repo.findAll();
    }

    private void apply(ProductUsage u, ProductUsageForm f) {
        u.setServiceCode(f.getServiceCode());
        u.setProductName(f.getProductName());
        u.setQuantityUsed(f.getQuantityUsed() == null ? 0 : f.getQuantityUsed());
        u.setPrice(f.getPrice() == null ? BigDecimal.ZERO : f.getPrice());

        // Optional fields – chỉ đặt khi entity có setter tương ứng
        safeSet(u, "setNote", new Class[]{String.class}, getIfExists(f, "getNote", String.class));
        safeSet(u, "setActive", new Class[]{boolean.class}, Boolean.TRUE);
    }

    // ===== helpers reflection an toàn =====
    private <T> T getIfExists(Object target, String getter, Class<T> type) {
        try {
            Method m = target.getClass().getMethod(getter);
            Object v = m.invoke(target);
            return type.isInstance(v) ? type.cast(v) : null;
        } catch (Exception ignored) { return null; }
    }

    private void safeSet(Object target, String setter, Class<?>[] paramTypes, Object value) {
        if (value == null) return;
        try {
            Method m = target.getClass().getMethod(setter, paramTypes);
            m.setAccessible(true);
            m.invoke(target, value);
        } catch (Exception ignored) { /* field không tồn tại thì bỏ qua */ }
    }
}
