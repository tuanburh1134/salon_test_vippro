package com.example.salon_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileUploadConfig {
    public FileUploadConfig(@Value("${app.upload.dir}") String dir) throws Exception {
        Path p = Paths.get(dir);
        if (!Files.exists(p)) Files.createDirectories(p);
    }
}