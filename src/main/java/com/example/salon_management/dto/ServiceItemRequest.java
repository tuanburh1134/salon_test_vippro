package com.example.salon_management.dto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import com.example.salon_management.entity.ServiceItem;

public class ServiceItemRequest {

    @NotBlank(message = "Tên không được để trống")
    @Size(max = 100, message = "Tên tối đa 100 ký tự")
    private String name;
    @Size(max = 50, message = "Loại tối đa 50 ký tự")
    @NotBlank(message = "Loại không được để trống")
    private String type;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.01", message = "Giá phải > 0")
    @Digits(integer = 12, fraction = 2, message = "Giá không hợp lệ")
    private BigDecimal price;

    @NotNull(message = "Thời gian không được để trống")
    @Min(value = 1, message = "Thời gian phải >= 1 phút")
    @Max(value = 600, message = "Thời gian tối đa 600 phút")
    private Integer durationMinutes;

    private boolean active = true;

    // file upload (tùy chọn)
    private MultipartFile image;
    public void normalize() {
        if (name != null) name = name.trim();
        if (type != null) type = type.trim();
    }
    // ==== factory: map từ entity -> DTO (phục vụ màn edit) ====
    public static ServiceItemRequest from(ServiceItem s) {
        ServiceItemRequest r = new ServiceItemRequest();
        r.setName(s.getName());
        r.setType(s.getType());
        r.setPrice(s.getPrice());
        r.setDurationMinutes(s.getDurationMinutes());
        r.setActive(s.isActive());
        // image không map ngược (người dùng sẽ chọn ảnh mới nếu cần)
        return r;
    }

    // ==== helper: copy từ DTO sang entity (tạo/sửa) ====
    public void copyTo(ServiceItem s) {
        s.setName(this.name);
        s.setType(this.type);
        s.setPrice(this.price);
        s.setDurationMinutes(this.durationMinutes);
        s.setActive(this.active);
        // image xử lý riêng trong service (lưu file -> setImagePath)
    }

    // getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }
}
