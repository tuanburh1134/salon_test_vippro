package com.example.salon_management.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.salon_management.dto.ServiceItemRequest;
import com.example.salon_management.entity.ServiceItem;
import com.example.salon_management.repository.ServiceItemRepository;
import com.example.salon_management.service.FileUploadService;
import com.example.salon_management.service.ServiceItemService;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceItemServiceImpl implements ServiceItemService {

    private final ServiceItemRepository repo;
    private final FileUploadService uploadService;

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceItem> search(String keyword, String type, Pageable pageable) {
        return repo.search(blankToNull(keyword), blankToNull(type), pageable);
    }

    @Override
    public ServiceItem create(ServiceItemRequest req) {
        ServiceItem s = new ServiceItem();
        apply(s, req);
        return repo.save(s);
    }

    @Override
    public ServiceItem update(Long id, ServiceItemRequest req) {
        ServiceItem s = repo.findById(id).orElseThrow();
        apply(s, req);
        return repo.save(s);
    }

    @Override
    public void delete(Long id) { repo.deleteById(id); }

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<ServiceItem> findById(Long id) { return repo.findById(id); }

    private void apply(ServiceItem s, ServiceItemRequest req) {
        s.setName(req.getName().trim());
        s.setType(req.getType().trim());
        s.setPrice(req.getPrice() == null ? BigDecimal.ZERO : req.getPrice());
        s.setDurationMinutes(req.getDurationMinutes());
        s.setActive(req.isActive());
        String path = uploadService.saveImage(req.getImage());
        if (path != null) s.setImagePath(path);
    }

    private String blankToNull(String s){ return (s==null||s.isBlank())?null:s; }
}
