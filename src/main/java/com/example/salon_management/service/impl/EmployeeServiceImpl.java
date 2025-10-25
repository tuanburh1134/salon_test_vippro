package com.example.salon_management.service.impl;

import com.example.salon_management.entity.Employee;
import com.example.salon_management.repository.EmployeeRepository;
import com.example.salon_management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repo;

    @Override
    public Page<Employee> search(String keyword, Pageable pageable) {
        if (keyword == null) keyword = "";
        return repo.search(keyword, pageable);
    }

    @Override
    public Employee get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
    }

    @Override
    public Employee create(Employee e) {
        e.setDeleted(false);
        return repo.save(e);
    }

    @Override
    public Employee update(Long id, Employee e) {
        Employee old = get(id);
        old.setName(e.getName());
        old.setPosition(e.getPosition());
        old.setSpecialty(e.getSpecialty());
        old.setShift(e.getShift());
        old.setSalary(e.getSalary());
        return repo.save(old);
    }

    @Override
    public void delete(Long id) {
        Employee e = get(id);
        e.setDeleted(true);
        repo.save(e);
    }
}
