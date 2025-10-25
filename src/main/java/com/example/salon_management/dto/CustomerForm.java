package com.example.salon_management.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerForm {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @Size(max = 15, message = "SĐT tối đa 15 ký tự")
    private String phone;

    @Email(message = "Email không hợp lệ")
    @Size(max = 120, message = "Email tối đa 120 ký tự")
    private String email;

    @Size(max = 30, message = "Loại thành viên tối đa 30 ký tự")
    private String memberType;

    @Min(value = 0, message = "Điểm tích lũy phải >= 0")
    private Integer point = 0;
}
