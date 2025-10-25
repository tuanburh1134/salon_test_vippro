package com.example.salon_management.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "product_usage")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Mã dịch vụ (Ma DV) – bạn có thể map sang bảng Service nếu có
    @Column(name = "service_code", nullable = false, length = 50)
    private String serviceCode;
    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;
    @Min(1)
    @Column(name = "quantity_used", nullable = false)
    private Integer quantityUsed;
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @PrePersist
    void preInsert() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
