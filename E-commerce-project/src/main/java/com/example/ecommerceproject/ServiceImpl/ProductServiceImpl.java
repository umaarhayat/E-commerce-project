package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.MerchantStore;
import com.example.ecommerceproject.Entity.Product;
import com.example.ecommerceproject.Exception.ProductNOtFoundException;
import com.example.ecommerceproject.Repository.MerchantStoreRepo;
import com.example.ecommerceproject.Repository.ProductRepo;
import com.example.ecommerceproject.Service.ProductService;
import com.example.ecommerceproject.converter.StoreConverter;
import com.example.ecommerceproject.dto.ReadAbleProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private MerchantStoreRepo merchantStoreRepo;
    @Autowired
    private StoreConverter storeConverter;

    @Override
    public ReadAbleProduct createProduct(Product product) {
        Product savedProduct = productRepo.save(product);
        return storeConverter.convertToReadable(savedProduct);
    }

    @Override
    public List<ReadAbleProduct> getAllProducts() {
        List<Product> products = productRepo.findByIsDeleteFalse();
        if (products.isEmpty()) throw new ProductNOtFoundException("No products found");

        List<ReadAbleProduct> list = new ArrayList<>();
        for (Product p : products) {
            list.add(storeConverter.convertToReadable(p));
        }
        return list;
    }

    @Override
    public ReadAbleProduct getProductById(Long id) {
        Product product = productRepo.findByIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found with ID: " + id));
        return storeConverter.convertToReadable(product);
    }

    @Override
    public ReadAbleProduct updateProduct(Long id, Product productDetails) {
        Product existing = productRepo.findByIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found with ID: " + id));

        existing.setSku(productDetails.getSku());
        existing.setRefSku(productDetails.getRefSku());
        existing.setAvailable(productDetails.isAvailable());
        existing.setActive(productDetails.isActive());
        existing.setPrice(productDetails.getPrice());
        existing.setQuantity(productDetails.getQuantity());
        existing.setDateAvailable(productDetails.getDateAvailable());
        existing.setCategory(productDetails.getCategory());
        existing.setCategoryId(productDetails.getCategoryId());

        // ----- Safely set merchantStore using merchantStoreId -----
        if (productDetails.getMerchantStoreId() != null) {
            MerchantStore store = merchantStoreRepo.findById(productDetails.getMerchantStoreId())
                    .orElseThrow(() -> new RuntimeException("Merchant Store not found with ID: " + productDetails.getMerchantStoreId()));
            existing.setMerchantStore(store);
        }

        Product updated = productRepo.save(existing);
        return storeConverter.convertToReadable(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product existing = productRepo.findByIdAndIsDeleteFalse(id)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found with ID: " + id));
        existing.setDelete(true);
        productRepo.save(existing);
    }
}
