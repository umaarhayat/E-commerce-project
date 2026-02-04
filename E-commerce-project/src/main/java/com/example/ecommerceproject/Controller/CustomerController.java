package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Entity.Customer;
import com.example.ecommerceproject.Service.CustomerService;
import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.PageResponse;
import com.example.ecommerceproject.dto.ReadAbleCustomer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer API", description = "Endpoints for managing customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ================= CREATE CUSTOMER =================
    @PostMapping
    @Operation(summary = "Create a new customer", description = "Adds a new customer to the system")
    public GenericResponse<ReadAbleCustomer> createCustomer(
            @RequestBody Customer customer
    ) {
        return GenericResponse.success(
                customerService.createCustomer(customer),
                "Customer created successfully"
        );
    }

    // ================= GET ALL CUSTOMERS =================
    @GetMapping
    @Operation(summary = "Get all customers (paginated)", description = "Fetch all customers with pagination")
    public GenericResponse<PageResponse<ReadAbleCustomer>> getAllCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<ReadAbleCustomer> response = customerService.getAllCustomers(page, size);
        return GenericResponse.success(response, "Customers fetched successfully");
    }

    // ================= GET CUSTOMER BY ID =================
    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Fetches a single customer using their ID")
    public GenericResponse<ReadAbleCustomer> getCustomerById(@PathVariable Long id) {
        return GenericResponse.success(
                customerService.getCustomerById(id),
                "Customer fetched successfully"
        );
    }

    // ================= UPDATE CUSTOMER =================
    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates an existing customer's information")
    public GenericResponse<ReadAbleCustomer> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customerDetails
    ) {
        return GenericResponse.success(
                customerService.updateCustomer(id, customerDetails),
                "Customer updated successfully"
        );
    }

    // ================= DELETE CUSTOMER =================
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by their ID")
    public GenericResponse<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return GenericResponse.success(
                null,
                "Customer deleted successfully"
        );
    }

    // ================= UPLOAD FILE =================
    @PostMapping("/{customerId}/upload")
    @Operation(summary = "Upload file for customer", description = "Uploads a file related to a customer")
    public GenericResponse<String> uploadCustomerFile(
            @PathVariable Long customerId,
            @RequestParam("file") MultipartFile file
    ) {
        String filePath = customerService.uploadCustomerFile(customerId, file);
        return GenericResponse.success(
                filePath,
                "File uploaded successfully"
        );
    }
}
