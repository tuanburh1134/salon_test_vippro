package com.example.salon_management.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public class PromotionForm {
    private String code;
    private String name;
    private Integer percent; // 1..100

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endAt;

    private String status; // ACTIVE/INACTIVE

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
