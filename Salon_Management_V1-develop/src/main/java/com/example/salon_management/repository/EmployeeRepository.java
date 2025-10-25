package com.example.salon_management.repository;

import com.example.salon_management.entity.Employee;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("""
           SELECT e FROM Employee e
           WHERE e.deleted = false AND (
                LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(e.position) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(e.specialty) LIKE LOWER(CONCAT('%', :keyword, '%'))
           )
           """)
    Page<Employee> search(String keyword, Pageable pageable);
}