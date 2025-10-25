package com.example.salon_management.dto;

import com.example.salon_management.entity.PaymentMethod;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Form tạo thanh toán.
 * Hỗ trợ nhập bookingCode hoặc chọn bookingId (fallback).
 */
public class PaymentForm {

    /** Nhập mã đặt lịch (ưu tiên). */
    private String bookingCode;

    /** Fallback nếu không nhập code. */
    private Long bookingId;

    /** Chọn KH (bỏ trống sẽ lấy theo booking). */
    private Long customerId;

    /** Chọn khuyến mãi. */
    private Long promotionId;

    /** Các dịch vụ tính tiền. */
    private List<Long> serviceItemIds;

    /** Các sản phẩm sử dụng tính tiền. */
    private List<Long> productUsageIds;

    /** Phương thức thanh toán (enum). */
    private PaymentMethod method;

    /** Thời điểm thanh toán (bind từ input type=datetime-local). */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime paidAt;

    // ======= GET/SET =======
    public String getBookingCode() { return bookingCode; }
    public void setBookingCode(String bookingCode) { this.bookingCode = bookingCode; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getPromotionId() { return promotionId; }
    public void setPromotionId(Long promotionId) { this.promotionId = promotionId; }

    public List<Long> getServiceItemIds() { return serviceItemIds; }
    public void setServiceItemIds(List<Long> serviceItemIds) { this.serviceItemIds = serviceItemIds; }

    public List<Long> getProductUsageIds() { return productUsageIds; }
    public void setProductUsageIds(List<Long> productUsageIds) { this.productUsageIds = productUsageIds; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
}
