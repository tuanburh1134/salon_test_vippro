package com.example.salon_management.service;

import com.example.salon_management.entity.Employee;
import org.springframework.data.domain.*;

public interface EmployeeService {
    Page<Employee> search(String keyword, Pageable pageable);
    Employee get(Long id);
    Employee create(Employee e);
    Employee update(Long id, Employee e);
    void delete(Long id);
}
