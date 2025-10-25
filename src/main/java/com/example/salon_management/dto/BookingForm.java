package com.example.salon_management.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

public class BookingForm {
    private String code;         // mã đặt lịch
    private Long customerId;
    private List<Long> serviceItemIds;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime bookedAt;

    private String status;       // CHO_XU_LY | XAC_NHAN | HOAN_THANH | HUY

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<Long> getServiceItemIds() { return serviceItemIds; }
    public void setServiceItemIds(List<Long> serviceItemIds) { this.serviceItemIds = serviceItemIds; }

    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
