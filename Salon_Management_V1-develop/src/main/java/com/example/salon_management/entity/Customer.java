package com.example.salon_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ======= THÔNG TIN KHÁCH HÀNG =======
    @NotBlank(message = "Tên không được để trống")
    @Column(nullable = false, length = 120)
    private String name;

    @Size(max = 15, message = "Số điện thoại tối đa 15 ký tự")
    @Column(length = 15)
    private String phone;

    @Email(message = "Email không hợp lệ")
    @Size(max = 120)
    @Column(length = 120)
    private String email;

    @Size(max = 30)
    @Column(length = 30)
    private String memberType; // Thường, VIP, Vàng, Bạch kim

    @Min(0)
    private Integer point = 0;

    // ======= TRẠNG THÁI =======
    @Column(nullable = false)
    private Boolean deleted = false;

    // ======= THỜI GIAN =======
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
