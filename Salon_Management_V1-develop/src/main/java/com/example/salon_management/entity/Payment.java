package com.example.salon_management.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã phiếu thanh toán (nếu bạn có, có thể null)
    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // >>> THÊM FIELD NÀY <<<
    @Column(name = "booking_code", nullable = false, length = 100)
    private String bookingCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING) // đảm bảo lưu enum dạng STRING
    @Column(name = "method")
    private PaymentMethod method;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    // Nếu bạn đã có mapping ManyToMany/OneToMany cho services/products thì giữ nguyên.
    @ManyToMany
    @JoinTable(name = "payment_service_items",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_item_id"))
    private Set<ServiceItem> serviceItems = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "payment_product_usages",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "product_usage_id"))
    private Set<ProductUsage> productUsages = new HashSet<>();

    // === Getter/Setter ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }

    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

    public Promotion getPromotion() { return promotion; }
    public void setPromotion(Promotion promotion) { this.promotion = promotion; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Set<ServiceItem> getServiceItems() { return serviceItems; }
    public void setServiceItems(Set<ServiceItem> serviceItems) { this.serviceItems = serviceItems; }

    public Set<ProductUsage> getProductUsages() { return productUsages; }
    public void setProductUsages(Set<ProductUsage> productUsages) { this.productUsages = productUsages; }
}
