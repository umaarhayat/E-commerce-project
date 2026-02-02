package com.example.ecommerceproject.ServiceImpl;

import com.example.ecommerceproject.Entity.Customer;
import com.example.ecommerceproject.Exception.CustomerNotFoundException;
import com.example.ecommerceproject.Repository.CustomerRepo;
import com.example.ecommerceproject.Service.CustomerService;
import com.example.ecommerceproject.converter.StoreConverter;
import com.example.ecommerceproject.dto.ReadAbleCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

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
    public List<ReadAbleCustomer> getAllCustomers() {
        List<Customer> customers = customerRepo.findAll();

        if (customers.isEmpty()) {
            throw new RuntimeException("No customers found");
        }

        List<ReadAbleCustomer> list = new ArrayList<>();
        for (Customer c : customers) {
            list.add(storeConverter.convertToReadable(c));
        }
        return list;
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
}
