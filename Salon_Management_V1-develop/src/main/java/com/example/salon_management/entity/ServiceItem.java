package com.example.salon_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity @Table(name="service_items")
@Getter @Setter
public class ServiceItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(nullable=false, length=150)
    private String name;

    @NotBlank @Column(nullable=false, length=80)
    private String type; // cắt tóc, nail, chăm sóc da...

    @NotNull @DecimalMin(value="0.0", inclusive=false) @Digits(integer=12,fraction=2)
    @Column(nullable=false, precision=14, scale=2)
    private BigDecimal price;

    @Min(1)
    private int durationMinutes; // phút

    private String imagePath; // /uploads/xxx.jpg

    @Column(nullable=false)
    private boolean active = true;
}
