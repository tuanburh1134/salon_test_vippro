package com.example.salon_management.repository;



import com.example.salon_management.entity.ProductUsage;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.*;

import org.springframework.data.repository.query.Param;

public interface ProductUsageRepository extends JpaRepository<ProductUsage, Long> {

    @Query("""

        SELECT p FROM ProductUsage p

        WHERE 

            (:kw IS NULL OR :kw = '' OR 

             LOWER(p.productName) LIKE LOWER(CONCAT('%', :kw, '%')) OR

             LOWER(p.serviceCode) LIKE LOWER(CONCAT('%', :kw, '%')))

        """)

    Page<ProductUsage> search(@Param("kw") String keyword, Pageable pageable);

}

