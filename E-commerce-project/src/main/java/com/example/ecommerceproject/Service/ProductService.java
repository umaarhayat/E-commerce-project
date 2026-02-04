package com.example.ecommerceproject.Service;

import com.example.ecommerceproject.Entity.Product;
import com.example.ecommerceproject.dto.PageResponse;
import com.example.ecommerceproject.dto.ReadAbleProduct;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // Create a new product
    ReadAbleProduct createProduct(Product product);

    // Get all products
//    PageResponse<ReadAbleProduct> getAllProducts(int page, int size);

    // Get a product by ID
    ReadAbleProduct getProductById(Long id);

    // Update a product by ID
    ReadAbleProduct updateProduct(Long id, Product productDetails);

    // Delete a product by ID
    void deleteProduct(Long id);

    // image crud operation
    String uploadProductImage(Long productId, MultipartFile file);

    Resource downloadProductImage(Long productId);

    String deleteProductImage(Long productId);

    // categoryId getAllProduct
    List<ReadAbleProduct> getProductsByCategoryId(Long categoryId);
    PageResponse<ReadAbleProduct> getProducts(
            String storeCode,
            String storeName,
            Long productId,
            String productName,
            String categoryName,
            Long categoryId,
            int pageNumber,
            int pageSize
    );
}
