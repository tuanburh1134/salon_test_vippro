package com.example.salon_management.dto;

import lombok.*;
import org.springframework.data.domain.Sort;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeSearchRequest {
    private String keyword = "";
    private int page = 0;
    private int size = 10;
    private String sortBy = "name";
    private String dir = "asc";

    public Sort getSort() {
        return "desc".equalsIgnoreCase(dir)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
    }
}
