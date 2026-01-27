package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Entity.Product;
import com.example.ecommerceproject.Service.ProductService;
import com.example.ecommerceproject.dto.ReadAbleProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ReadAbleProduct> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<ReadAbleProduct>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadAbleProduct> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadAbleProduct> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }



    // ================= UPLOAD PRODUCT IMAGE =================
    @PostMapping("/{productId}/image")
    public ResponseEntity<String> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file
    ) {
        String message = productService.uploadProductImage(productId, file);
        return ResponseEntity.ok(message);
    }

    // ================= DOWNLOAD PRODUCT IMAGE =================
    @GetMapping("/{productId}/image")
    public ResponseEntity<Resource> downloadProductImage(
            @PathVariable Long productId
    ) {
        Resource resource = productService.downloadProductImage(productId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // or OCTET_STREAM
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }

    // ================= DELETE PRODUCT IMAGE =================
    @DeleteMapping("/{productId}/image")
    public ResponseEntity<String> deleteProductImage(
            @PathVariable Long productId
    ) {
        String message = productService.deleteProductImage(productId);
        return ResponseEntity.ok(message);
    }
}
