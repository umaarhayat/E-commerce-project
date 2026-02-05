package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.Customer;
import com.example.ecommerceproject.Exception.CustomerNotFoundException;
import com.example.ecommerceproject.Repository.CustomerRepo;
import com.example.ecommerceproject.Service.CustomerService;
import com.example.ecommerceproject.Service.FileStorageService;
import com.example.ecommerceproject.converter.StoreConverter;
import com.example.ecommerceproject.dto.CustomerDto;
import com.example.ecommerceproject.dto.PageResponse;
import com.example.ecommerceproject.dto.ReadAbleCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystemNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private StoreConverter storeConverter;

    // ================= CREATE CUSTOMER =================
    @Override
    public ReadAbleCustomer createCustomer(Customer customer) {
        // ================= PASSWORD CHECK =================
        if (customer.getPassword() == null || customer.getPassword().isBlank()) {
            throw new RuntimeException("Password cannot be null or blank");
        }

        // ================= LINK CONTACTS =================
        if (customer.getContactDetails() != null && !customer.getContactDetails().isEmpty()) {
            customer.getContactDetails().forEach(contact -> contact.setCustomer(customer));
        }

        // ================= SAVE =================
        Customer saved = customerRepo.save(customer);

        return storeConverter.convertToReadable(saved);
    }

    // ================= GET ALL CUSTOMERS =================

    @Override
    public PageResponse<ReadAbleCustomer> getAllCustomers(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customers = customerRepo.findAll(pageable);

        if (customers.isEmpty()) {
            throw new CustomerNotFoundException("No customers found");
        }

        List<ReadAbleCustomer> dtoList = new ArrayList<>();
        for (Customer c : customers.getContent()) {
            dtoList.add(storeConverter.convertToReadable(c));
        }

        PageResponse<ReadAbleCustomer> response = new PageResponse<>();
        response.setContent(dtoList);
        response.setPage(page);
        response.setSize(size);
        response.setTotalPages(customers.getTotalPages());
        response.setTotalElements(customers.getTotalElements());

        return response;
    }


    // ================= GET CUSTOMER BY ID =================
    @Override
    public ReadAbleCustomer getCustomerById(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        return storeConverter.convertToReadable(customer);
    }

    // ================= UPDATE CUSTOMER =================
    @Override
    public ReadAbleCustomer updateCustomer(Long id, Customer customerDetails) {
        Customer existing = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));

        // ================= MAP BASIC FIELDS =================
        if (customerDetails.getCompanyName() != null) existing.setCompanyName(customerDetails.getCompanyName());
        if (customerDetails.getCompanyWebsite() != null) existing.setCompanyWebsite(customerDetails.getCompanyWebsite());
        if (customerDetails.getBusinessType() != null) existing.setBusinessType(customerDetails.getBusinessType());
        if (customerDetails.getCustomerType() != null) existing.setCustomerType(customerDetails.getCustomerType());
        if (customerDetails.getPassword() != null) existing.setPassword(customerDetails.getPassword());

        // ================= CONTACT DETAILS =================
        if (customerDetails.getContactDetails() != null && !customerDetails.getContactDetails().isEmpty()) {
            // Clear old contacts
            existing.getContactDetails().clear();

            // Add new contacts from incoming entity
            existing.getContactDetails().addAll(customerDetails.getContactDetails());

            // Link each contact back to parent
            existing.getContactDetails().forEach(contact -> contact.setCustomer(existing));
        }

        // ================= SAVE AND RETURN =================
        Customer updated = customerRepo.save(existing);
        return storeConverter.convertToReadable(updated);
    }

    // ================= DELETE CUSTOMER =================
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + id));
        customerRepo.delete(customer);
    }

    @Override
    public String uploadCustomerFile(Long customerId, MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".pdf")) {
            throw new FileSystemNotFoundException("Only PDF files are allowed!"); // yahi message Postman me dikhayega
        }

        // Upload logic
        return fileStorageService.uploadFile(file, "customer_" + customerId, originalFileName);
    }

}
