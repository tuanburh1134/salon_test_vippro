package com.example.salon_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cột đang dùng trong hệ thống
    @Column(name = "booking_code", nullable = false, unique = true, length = 30)
    private String code;

    // ✅ BỔ SUNG: cột legacy 'code' trong DB (NOT NULL) -> ghi đồng bộ để khỏi lỗi
    @Column(name = "code", nullable = false, length = 30)
    private String legacyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "booking_service_items",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "service_item_id"))
    private Set<ServiceItem> serviceItems = new LinkedHashSet<>();

    @NotNull
    @Column(name = "booked_at", nullable = false)
    private LocalDateTime bookedAt;

    // DB có cột booking_time NOT NULL
    @Column(name = "booking_time", nullable = false)
    private LocalTime bookingTime;

    @Column(name = "status", length = 30, nullable = false)
    private String status;

    // DB có cột completed NOT NULL
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @PrePersist
    public void prePersist() {
        // Tự sinh code nếu chưa có (tránh NOT NULL + UNIQUE)
        if (this.code == null || this.code.isBlank()) {
            // Ví dụ: BK-yyMMddHHmmss-XXXX
            String rnd = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            this.code = "BK-" + java.time.format.DateTimeFormatter.ofPattern("yyMMddHHmmss")
                    .format(LocalDateTime.now()) + "-" + rnd;
        }
        // Đồng bộ cột legacy 'code' trong DB
        this.legacyCode = this.code;

        if (this.bookedAt == null) this.bookedAt = LocalDateTime.now();
        if (this.bookingTime == null) this.bookingTime = this.bookedAt.toLocalTime();
        if (this.status == null) this.status = "CHO_XU_LY";
        if (this.completed == null) this.completed = Boolean.FALSE;
    }

    @PreUpdate
    public void preUpdate() {
        if (this.legacyCode == null || this.legacyCode.isBlank()) {
            this.legacyCode = this.code;
        }
        if (this.bookingTime == null && this.bookedAt != null) {
            this.bookingTime = this.bookedAt.toLocalTime();
        }
        if (this.completed == null) this.completed = Boolean.FALSE;
    }

    // ===== getters & setters =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getLegacyCode() { return legacyCode; }
    public void setLegacyCode(String legacyCode) { this.legacyCode = legacyCode; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Set<ServiceItem> getServiceItems() { return serviceItems; }
    public void setServiceItems(Set<ServiceItem> serviceItems) { this.serviceItems = serviceItems; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
        if (bookedAt != null && this.bookingTime == null) {
            this.bookingTime = bookedAt.toLocalTime();
        }
    }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}
