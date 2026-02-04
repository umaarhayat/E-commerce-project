package com.example.ecommerceproject.Service;


import com.example.ecommerceproject.Entity.Customer;
import com.example.ecommerceproject.dto.CustomerDto;
import com.example.ecommerceproject.dto.PageResponse;
import com.example.ecommerceproject.dto.ReadAbleCustomer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {

    ReadAbleCustomer createCustomer(Customer customer);

    PageResponse<ReadAbleCustomer> getAllCustomers(int page, int size);

    ReadAbleCustomer getCustomerById(Long id);

    ReadAbleCustomer updateCustomer(Long id, Customer customer);

    void deleteCustomer(Long id);


    String uploadCustomerFile(Long customerId, MultipartFile file);
}
