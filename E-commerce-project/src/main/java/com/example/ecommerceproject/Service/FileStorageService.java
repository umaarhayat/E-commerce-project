package com.example.ecommerceproject.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    // Upload file
    String uploadFile(MultipartFile file, String directory, String fileName);

    // Download file
    Resource downloadFile(String directory, String fileName);

    // Delete file
    boolean deleteFile(String directory, String fileName);

    // Check if file exists
    boolean fileExists(String directory, String fileName);
}
