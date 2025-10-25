package com.example.salon_management.service.impl;

import com.example.salon_management.dto.PaymentForm;
import com.example.salon_management.entity.*;
import com.example.salon_management.repository.*;
import com.example.salon_management.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;
    private final PromotionRepository promotionRepo;
    private final CustomerRepository customerRepo;
    private final ServiceItemRepository serviceItemRepo;
    private final ProductUsageRepository productUsageRepo;

    public PaymentServiceImpl(PaymentRepository paymentRepo,
                              BookingRepository bookingRepo,
                              PromotionRepository promotionRepo,
                              CustomerRepository customerRepo,
                              ServiceItemRepository serviceItemRepo,
                              ProductUsageRepository productUsageRepo) {
        this.paymentRepo = paymentRepo;
        this.bookingRepo = bookingRepo;
        this.promotionRepo = promotionRepo;
        this.customerRepo = customerRepo;
        this.serviceItemRepo = serviceItemRepo;
        this.productUsageRepo = productUsageRepo;
    }

    @Override
    public Payment create(PaymentForm form) {
        Payment p = new Payment();

        // ===== Booking (yêu cầu có bookingCode) =====
        if (form.getBookingCode() == null || form.getBookingCode().isBlank()) {
            throw new IllegalArgumentException("Thiếu mã đặt lịch (bookingCode).");
        }
        Booking booking = bookingRepo.findByCode(form.getBookingCode().trim())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã đặt lịch: " + form.getBookingCode()));
        p.setBooking(booking);
        p.setBookingCode(booking.getCode());

        // ===== Khách hàng =====
        if (form.getCustomerId() != null) {
            Customer c = customerRepo.findById(form.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng id=" + form.getCustomerId()));
            p.setCustomer(c);
        } else {
            // mặc định theo khách của booking
            p.setCustomer(booking.getCustomer());
        }

        // ===== Khuyến mãi =====
        if (form.getPromotionId() != null) {
            Promotion promo = promotionRepo.findById(form.getPromotionId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khuyến mãi id=" + form.getPromotionId()));
            p.setPromotion(promo);
        }

        // ===== Dịch vụ / Sản phẩm =====
        if (form.getServiceItemIds() != null && !form.getServiceItemIds().isEmpty()) {
            p.getServiceItems().addAll(serviceItemRepo.findAllById(form.getServiceItemIds()));
        }
        if (form.getProductUsageIds() != null && !form.getProductUsageIds().isEmpty()) {
            p.getProductUsages().addAll(productUsageRepo.findAllById(form.getProductUsageIds()));
        }

        // ===== Phương thức thanh toán (enum) =====
        // PaymentForm.method đã là PaymentMethod -> gán trực tiếp, KHÔNG gọi toUpperCase()
        p.setMethod(form.getMethod());

        // ===== Thời điểm thanh toán =====
        p.setPaidAt(form.getPaidAt() != null ? form.getPaidAt() : LocalDateTime.now());

        // ===== TÍNH TỔNG =====
        // Tổng dịch vụ
        BigDecimal serviceTotal = p.getServiceItems().stream()
                .map(si -> si.getPrice() == null ? BigDecimal.ZERO : si.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tổng sản phẩm sử dụng (đơn giá * số lượng)
        BigDecimal productTotal = p.getProductUsages().stream()
                .map(u -> unitPrice(u).multiply(BigDecimal.valueOf(quantity(u))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal gross = serviceTotal.add(productTotal);

        int percent = (p.getPromotion() != null && p.getPromotion().getPercent() != null)
                ? p.getPromotion().getPercent() : 0;

        BigDecimal discount = gross.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100));
        BigDecimal net = gross.subtract(discount);

        p.setTotalAmount(net.max(BigDecimal.ZERO));

        // Lưu
        return paymentRepo.save(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> search(LocalDateTime from, LocalDateTime to) {
        if (from != null && to != null) {
            return paymentRepo.findByPaidAtBetween(from, to);
        }
        return paymentRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        return paymentRepo.findAll();
    }

    // ================== Helpers ==================
    private BigDecimal toBigDecimal(Object v) {
        if (v == null) return BigDecimal.ZERO;
        if (v instanceof BigDecimal) return (BigDecimal) v;
        if (v instanceof Number) return BigDecimal.valueOf(((Number) v).doubleValue());
        try { return new BigDecimal(v.toString()); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    private Object tryInvoke(Object target, String... methodNames) {
        for (String name : methodNames) {
            try {
                Method m = target.getClass().getMethod(name);
                m.setAccessible(true);
                return m.invoke(target);
            } catch (Exception ignored) {}
        }
        return null;
    }

    // Lấy đơn giá từ ProductUsage (ưu tiên getPrice, fallback các tên khác)
    private BigDecimal unitPrice(Object productUsage) {
        // Nếu đúng type, đọc trực tiếp cho nhanh
        if (productUsage instanceof ProductUsage u) {
            return u.getPrice() == null ? BigDecimal.ZERO : u.getPrice();
        }
        Object v = tryInvoke(productUsage,
                "getPrice", "getUnitPrice", "getUnitCost", "getCost", "getGia", "getDonGia");
        return toBigDecimal(v);
    }

    // Lấy số lượng từ ProductUsage (ưu tiên getQuantityUsed, fallback các tên khác)
    private int quantity(Object productUsage) {
        if (productUsage instanceof ProductUsage u) {
            Integer q = u.getQuantityUsed();
            return q == null ? 1 : q;
        }
        Object v = tryInvoke(productUsage,
                "getQuantityUsed", "getQuantity", "getQty", "getAmount", "getUsedQuantity", "getSoLuong");
        if (v == null) return 1;
        if (v instanceof Number) return ((Number) v).intValue();
        try { return Integer.parseInt(v.toString()); } catch (Exception e) { return 1; }
    }
}
