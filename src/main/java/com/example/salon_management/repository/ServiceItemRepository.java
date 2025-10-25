package com.example.salon_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.salon_management.entity.ServiceItem;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    @Query("""
      SELECT s FROM ServiceItem s
      WHERE (:kw IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%',:kw,'%'))
                     OR LOWER(s.type) LIKE LOWER(CONCAT('%',:kw,'%')))
        AND (:type IS NULL OR LOWER(s.type) = LOWER(:type))
    """)
    Page<ServiceItem> search(String kw, String type, Pageable pageable);
}
