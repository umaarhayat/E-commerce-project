package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Entity.Product;
import com.example.ecommerceproject.Service.ProductService;
import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.ReadAbleProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public GenericResponse createProduct(@RequestBody Product product) {
        return GenericResponse.success(
                productService.createProduct(product),
                "Product created successfully"
        );
    }

    @GetMapping("/all")
    public GenericResponse getAllProducts() {
        return GenericResponse.success(
                productService.getAllProducts(),
                "Products fetched successfully"
        );
    }

    @GetMapping("/{id}")
    public GenericResponse getProductById(@PathVariable Long id) {
        return GenericResponse.success(
                productService.getProductById(id),
                "Product fetched successfully"
        );
    }

    @PutMapping("/{id}")
    public GenericResponse updateProduct(
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        return GenericResponse.success(
                productService.updateProduct(id, product),
                "Product updated successfully"
        );
    }

    @DeleteMapping("/{id}")
    public GenericResponse deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return GenericResponse.success(null, "Product deleted successfully"
        );
    }

    // ================= UPLOAD PRODUCT IMAGE =================
    @PostMapping("/{productId}/image/upload")
    public GenericResponse uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file
    ) {
        String imageName = productService.uploadProductImage(productId, file);

        return GenericResponse.success(imageName,
                "Product image uploaded successfully"
        );
    }

    // ================= DOWNLOAD PRODUCT IMAGE =================
    @GetMapping("/{productId}/image")
    public ResponseEntity<Resource> downloadProductImage(
            @PathVariable Long productId
    ) {
        Resource resource = productService.downloadProductImage(productId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }

    // ================= DELETE PRODUCT IMAGE =================
    @DeleteMapping("/{productId}/image")
    public GenericResponse deleteProductImage(
            @PathVariable Long productId
    ) {
        String deletedImage = productService.deleteProductImage(productId);

        return GenericResponse.success(
                deletedImage,
                "Product image deleted successfully"
        );
    }


    /**
     * GET /api/products
     * Optional filters:
     * storeCode, storeName, productId, productName, categoryId, categoryName
     * If no filter is passed, returns all products
     */
    @GetMapping("/search")
    public GenericResponse<List<ReadAbleProduct>> getProducts(
            @RequestParam(required = false) String storeCode,
            @RequestParam(required = false) String storeName,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Long categoryId
    ) {
        List<ReadAbleProduct> products = productService.getProducts(
                storeCode, storeName, productId, productName, categoryName, categoryId
        );

        return GenericResponse.success(products, "Products fetched successfully");
    }
}





