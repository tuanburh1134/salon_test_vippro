package com.example.salon_management.repository;

import com.example.salon_management.entity.Customer;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("""
           SELECT c FROM Customer c
           WHERE (:kw IS NULL OR :kw = '' OR
                 LOWER(c.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR
                 LOWER(c.phone) LIKE LOWER(CONCAT('%', :kw, '%')) OR
                 LOWER(c.email) LIKE LOWER(CONCAT('%', :kw, '%')))
           """)
    Page<Customer> search(@Param("kw") String keyword, Pageable pageable);
}
