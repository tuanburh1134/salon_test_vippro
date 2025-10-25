package com.example.salon_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String code;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    // Cột đang dùng thật sự
    @Min(1) @Max(100)
    @Column(name = "discount_percent", nullable = false)
    private Integer percent;

    // ✅ Thêm field "legacy" để lấp cột percent còn đang NOT NULL trong DB
    // Không dùng ở code, chỉ để đồng bộ khi insert/update cho khỏi lỗi DB
    @Column(name = "percent")
    private Integer legacyPercent;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Transient
    private String status;

    @PrePersist
    public void prePersist() {
        if (active == null) active = Boolean.TRUE;
        normalizePercent();
    }

    @PreUpdate
    public void preUpdate() {
        normalizePercent();
    }

    private void normalizePercent() {
        if (percent == null || percent < 1) percent = 1;
        if (percent > 100) percent = 100;

        // ghi đồng thời vào cột 'percent' để DB không kêu thiếu
        this.legacyPercent = this.percent;
    }

    // ===== getters & setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getPercent() { return percent; }
    public void setPercent(Integer percent) { this.percent = percent; }
    public LocalDateTime getStartAt() { return startAt; }
    public void setStartAt(LocalDateTime startAt) { this.startAt = startAt; }
    public LocalDateTime getEndAt() { return endAt; }
    public void setEndAt(LocalDateTime endAt) { this.endAt = endAt; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
