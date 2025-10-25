package com.example.salon_management.dto;

import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class ServiceSearchRequest {
    private String keyword;
    private String type;
    private String sortBy = "name"; // name | price | durationMinutes
    private String dir = "asc";     // asc | desc
    private int page = 0;
    private int size = 5;
}
