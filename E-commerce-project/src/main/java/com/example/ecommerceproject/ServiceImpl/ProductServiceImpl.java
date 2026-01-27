package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.Category;
import com.example.ecommerceproject.Entity.MerchantStore;
import com.example.ecommerceproject.Entity.Product;
import com.example.ecommerceproject.Entity.ProductDescription;
import com.example.ecommerceproject.Exception.CategoryNotFoundException;
import com.example.ecommerceproject.Exception.ProductNOtFoundException;
import com.example.ecommerceproject.Repository.CategoryRepo;
import com.example.ecommerceproject.Repository.MerchantStoreRepo;
import com.example.ecommerceproject.Repository.ProductRepo;
import com.example.ecommerceproject.Service.FileStorageService;
import com.example.ecommerceproject.Service.ProductService;
import com.example.ecommerceproject.converter.StoreConverter;
import com.example.ecommerceproject.dto.ReadAbleProduct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private MerchantStoreRepo merchantStoreRepo;

    @Autowired
    private StoreConverter storeConverter;

    @Autowired
    private FileStorageService fileStorageService;
    @Override
    public ReadAbleProduct createProduct(Product product) {
        // Set Category entity
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryRepo.findById(product.getCategory().getId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            "Category not found with ID: " + product.getCategory().getId()));
            product.setCategory(category);
        }

        // Set MerchantStore entity
        if (product.getMerchantStore() != null && product.getMerchantStore().getId() != null) {
            MerchantStore store = merchantStoreRepo.findById(product.getMerchantStore().getId())
                    .orElseThrow(() -> new RuntimeException(
                            "Merchant Store not found with ID: " + product.getMerchantStore().getId()));
            product.setMerchantStore(store);
        }

        // Set Product for each description
        if (product.getProductDescriptions() != null) {
            for (ProductDescription desc : product.getProductDescriptions()) {
                desc.setProduct(product); // link description to product
            }
        }
        Product saved = productRepo.save(product);
        return storeConverter.convertToReadable(saved); // Now categoryId & merchantStoreId won't be null
    }




    @Override
    public List<ReadAbleProduct> getAllProducts() {
        List<Product> products = productRepo.findAll();
        if (products.isEmpty()) throw new ProductNOtFoundException("No products found");

        return products.stream()
                .map(storeConverter::convertToReadable) // now includes descriptions
                .collect(Collectors.toList());
    }

    @Override
    public ReadAbleProduct getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found with ID: " + id));
        return storeConverter.convertToReadable(product);
    }

    @Override
    public ReadAbleProduct updateProduct(Long id, Product productDetails) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found with ID: " + id));

        existing.setProductImage(productDetails.getProductImage());
        existing.setSku(productDetails.getSku());
        existing.setRefSku(productDetails.getRefSku());
        existing.setAvailable(productDetails.isAvailable());
        existing.setActive(productDetails.isActive());
        existing.setPrice(productDetails.getPrice());
        existing.setQuantity(productDetails.getQuantity());
        existing.setDateAvailable(productDetails.getDateAvailable());
        existing.setCategory(productDetails.getCategory());
        existing.setMerchantStore(productDetails.getMerchantStore());

        // ===== Update descriptions if provided =====
        if (productDetails.getProductDescriptions() != null) {
            existing.setProductDescriptions(productDetails.getProductDescriptions());
        }

        Product updated = productRepo.save(existing);
        return storeConverter.convertToReadable(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        // Find existing product
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found with ID: " + id));

        // Remove product (descriptions will be deleted automatically if cascade = REMOVE is set)
        productRepo.delete(existing);
    }


    // image crud

    @Override
    public String uploadProductImage(Long productId, MultipartFile file) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String directory = "products";
        String fileName = file.getOriginalFilename();

        fileStorageService.uploadFile(file, directory, fileName);
        // YAHI LINE IMPORTANT HAI
        product.setProductImage(fileName);

        product.setUpdatedAt(new Date());
        productRepo.save(product);

        return "Product image uploaded successfully";
    }

    @Override
    public Resource downloadProductImage(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found"));

        if (product.getProductImage() == null || product.getProductImage().isBlank()) {
            throw new RuntimeException("No image exists for this product");
        }

        return fileStorageService.downloadFile(
                "products",
                product.getProductImage()
        );
    }
    @Override
    public String deleteProductImage(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNOtFoundException("Product not found"));

        if (product.getProductImage() == null || product.getProductImage().isBlank()) {
            return "No image exists for this product";
        }

        boolean deleted = fileStorageService.deleteFile(
                "products",
                product.getProductImage()
        );

        product.setProductImage(null);
        product.setUpdatedAt(new Date());
        productRepo.save(product);

        return deleted
                ? "Product image deleted successfully"
                : "Image not found on disk but DB updated";
    }


}
