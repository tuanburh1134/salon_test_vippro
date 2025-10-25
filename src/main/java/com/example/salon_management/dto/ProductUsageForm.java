package com.example.salon_management.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class ProductUsageForm {
    @NotBlank(message = "Mã DV không được trống")
    private String serviceCode;
    @NotBlank(message = "Tên sản phẩm không được trống")
    private String productName;
    @Min(value = 1, message = "Số lượng > 0")
    private Integer quantityUsed;
    @DecimalMin(value = "0.0", message = "Giá >= 0")
    private BigDecimal price;
}

