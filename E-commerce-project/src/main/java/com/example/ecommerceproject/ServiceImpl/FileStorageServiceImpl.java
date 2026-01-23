package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Override
    public String uploadFile(MultipartFile file, String directory, String fileName) {
        return null;
    }

    @Override
    public Resource downloadFile(String directory, String fileName) {
        return null;
    }

    @Override
    public boolean deleteFile(String directory, String fileName) {
        return false;
    }

    @Override
    public boolean fileExists(String directory, String fileName) {
        return false;
    }
}
