package com.example.ecommerceproject.Service;


import com.example.ecommerceproject.Entity.Customer;
import com.example.ecommerceproject.dto.CustomerDto;
import com.example.ecommerceproject.dto.ReadAbleCustomer;

import java.util.List;

public interface CustomerService {

    ReadAbleCustomer createCustomer(Customer customer);

    ReadAbleCustomer getCustomerById(Long id);

    List<ReadAbleCustomer> getAllCustomers();

    ReadAbleCustomer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);
}
