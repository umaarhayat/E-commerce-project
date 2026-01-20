package com.example.ecommerceproject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.FileInputStream;

@SpringBootApplication
public class ECommerceProjectApplication {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    public static void main(String[] args) {
        SpringApplication.run(ECommerceProjectApplication.class, args);

//        try {
//
//
//            int a = 100, b = 0;
//
//            int c = a / b;
//
//            System.out.println(c);
//        }
//
//        catch (Exception e){
//
//            System.out.println(e);
//            e.printStackTrace();
//            System.out.println("you cannot divided by zero  so plz you provide other value..");
//        }
//
//        finally {
//            System.out.println("i am in the finally block");
//        }
//
    }

}
