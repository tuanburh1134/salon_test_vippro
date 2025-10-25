package com.example.salon_management.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String saveImage(MultipartFile file); // trả về path tương đối (vd: /uploads/xxx.jpg)
}
