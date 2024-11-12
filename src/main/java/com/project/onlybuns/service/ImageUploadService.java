package com.project.onlybuns.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageUploadService {
    private static final String UPLOAD_DIR = "src/main/resources/static.images";

    public String uploadImage(MultipartFile file) throws IOException {
        String fileType = file.getContentType();
        if (fileType == null || !fileType.startsWith("image")) {
            throw new IllegalArgumentException("File is not an image");
        }

        String fileName = file.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR, fileName);

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create upload directory");
        }

        Files.copy(file.getInputStream(), uploadPath);

        return "/images/" + fileName;
    }
}
