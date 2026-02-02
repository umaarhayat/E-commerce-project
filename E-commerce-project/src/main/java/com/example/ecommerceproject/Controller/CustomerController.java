package com.example.ecommerceproject.Controller;

import com.example.ecommerceproject.Entity.Customer;
import com.example.ecommerceproject.Service.CustomerService;
import com.example.ecommerceproject.dto.GenericResponse;
import com.example.ecommerceproject.dto.ReadAbleCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // ================= CREATE CUSTOMER =================
    @PostMapping
    public GenericResponse<ReadAbleCustomer> createCustomer(@RequestBody Customer customer) {
        return GenericResponse.success(
                customerService.createCustomer(customer),
                "Customer created successfully"
        );
    }

    // ================= GET ALL CUSTOMERS =================
    @GetMapping
    public GenericResponse<List<ReadAbleCustomer>> getAllCustomers() {
        return GenericResponse.success(
                customerService.getAllCustomers(),
                "Customers fetched successfully"
        );
    }

    // ================= GET CUSTOMER BY ID =================
    @GetMapping("/{id}")
    public GenericResponse<ReadAbleCustomer> getCustomerById(@PathVariable Long id) {
        return GenericResponse.success(
                customerService.getCustomerById(id),
                "Customer fetched successfully"
        );
    }

    // ================= UPDATE CUSTOMER =================
    @PutMapping("/{id}")
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
    public GenericResponse<String> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return GenericResponse.success(
                null,
                "Customer deleted successfully"
        );
    }
}
