package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path rootLocation = Paths.get("uploads"); // Base folder for all files

    public FileStorageServiceImpl() {
        try {
            Files.createDirectories(rootLocation); // Ensure base folder exists
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage folder", e);
        }
    }

    // Helper method to get safe directory path and create folder if it doesn't exist
    private Path getSafeDirectory(String directory) {
        Path dirPath = rootLocation.resolve(directory.replaceAll("\\s+", "_")).normalize();
        try {
            Files.createDirectories(dirPath); // Create folder if not exists
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory: " + directory, e);
        }
        return dirPath;
    }

    @Override
    public String uploadFile(MultipartFile file, String directory, String fileName) {
        try {
            Path dirPath = getSafeDirectory(directory);
            Path targetLocation = dirPath.resolve(fileName).normalize();

            // Copy file to target location (replace if exists)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString(); // Return full path (optional)
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName, e);
        }
    }

    @Override
    public Resource downloadFile(String directory, String fileName) {
        try {
            Path filePath = getSafeDirectory(directory).resolve(fileName).normalize();
            if (!Files.exists(filePath)) throw new RuntimeException("File not found: " + fileName);

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not readable: " + fileName);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("File path error: " + fileName, e);
        }
    }

    @Override
    public boolean deleteFile(String directory, String fileName) {
        try {
            Path filePath = getSafeDirectory(directory).resolve(fileName).normalize();
            if (!Files.exists(filePath)) return false; // File not found
            Files.delete(filePath);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + fileName, e);
        }
    }

    @Override
    public boolean fileExists(String directory, String fileName) {
        Path filePath = getSafeDirectory(directory).resolve(fileName).normalize();
        return Files.exists(filePath) && Files.isReadable(filePath);
    }
}
