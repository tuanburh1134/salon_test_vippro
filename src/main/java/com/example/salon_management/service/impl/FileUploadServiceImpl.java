package com.example.salon_management.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.salon_management.service.FileUploadService;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${app.upload.dir}") private String uploadDir;

    @Override
    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            String ext = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(f.lastIndexOf(".")))
                    .orElse(".jpg");
            String filename = UUID.randomUUID() + ext;
            Path dest = Paths.get(uploadDir).resolve(filename).normalize();
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
            return "/" + uploadDir + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Upload ảnh thất bại", e);
        }
    }
}