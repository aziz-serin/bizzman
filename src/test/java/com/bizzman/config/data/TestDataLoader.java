/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.config.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bizzman.dao.services.ProductService;
import com.bizzman.dao.services.SupplierService;
import com.bizzman.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bizzman.dao.services.EmployeeService;

@Configuration
@Profile("test")
public class TestDataLoader {
    private final static Logger log = LoggerFactory.getLogger(TestDataLoader.class);
    @Autowired
    EmployeeService employeeService;

    @Autowired
    ProductService productService;

    @Autowired
    SupplierService supplierService;

    @Bean
    CommandLineRunner initDatabase(){

        Employee employee1 = new Employee();
        PersonalDetails personalDetails1 = new PersonalDetails();
        EmergencyContactDetails emergencyContactDetail1 = new EmergencyContactDetails();
        Employee employee2 = new Employee();
        PersonalDetails personalDetails2 = new PersonalDetails();
        EmergencyContactDetails emergencyContactDetail2 = new EmergencyContactDetails();

        employee1.setName("Aziz");
        employee1.setJoiningDate( LocalDate.of(2021, 10, 9));
        employee1.setSalary(33600);
        employee1.setNationalInsurance("This is NI");
        employee1.setOther_expenses(1000);
        personalDetails1.setAddress("this street");
        personalDetails1.setBirthDate( LocalDate.of(2002, 3, 2));
        personalDetails1.setPhoneNumber("84302819489201");
        personalDetails1.setPassportNumber("84930289408239");
        employee1.setPersonalDetails(personalDetails1);
        emergencyContactDetail1.setName("Maria");
        emergencyContactDetail1.setRelationship(EmergencyContactDetails.Relationship.PARTNER);
        emergencyContactDetail1.setPhoneNumber("89028139012");
        employee1.setEmergencyContactDetails(new ArrayList<>(List.of(emergencyContactDetail1)));

        employee2.setName("B");
        employee2.setJoiningDate( LocalDate.of(2019, 10, 9));
        employee2.setSalary(45600);
        employee2.setNationalInsurance("This is NI");
        employee2.setOther_expenses(3000);
        personalDetails2.setAddress("this street");
        personalDetails2.setBirthDate( LocalDate.of(1992, 10, 9));
        personalDetails2.setPhoneNumber("84302819489501");
        personalDetails2.setPassportNumber("84930289408239");
        employee2.setPersonalDetails(personalDetails2);
        emergencyContactDetail2.setName("Maria");
        emergencyContactDetail2.setRelationship(EmergencyContactDetails.Relationship.PARTNER);
        emergencyContactDetail1.setPhoneNumber("89028139012");
        employee2.setEmergencyContactDetails(new ArrayList<>(List.of(emergencyContactDetail2)));

        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Supplier supplier1 = new Supplier();
        Supplier supplier2 = new Supplier();

        product1.setCategory(Product.ProductCategory.PRICE_BY_WEIGHT);
        product1.setArrivalDate(LocalDate.of(2022, 10, 9));
        product1.setQuantity(5);
        product1.setStockWeight(100.8);
        product1.setUnitPrice(40);
        supplier1.setContacts(Map.of("Ahmed", "738219321", "Melissa", "39382948490"));
        supplier1.setName("Alu");
        product1.setSupplier(supplier1);

        product2.setCategory(Product.ProductCategory.PRICE_BY_QUANTITY);
        product2.setArrivalDate(LocalDate.of(2022, 10, 17));
        product2.setQuantity(100);
        product2.setStockWeight(21.2);
        product2.setUnitPrice(5);
        supplier2.setContacts(Map.of("Jen", "738439321", "Ben", "39382948490"));
        supplier2.setName("Acc");
        product2.setSupplier(supplier2);

        product3.setCategory(Product.ProductCategory.PRICE_BY_WEIGHT);
        product3.setArrivalDate(LocalDate.of(2022, 10, 5));
        product3.setQuantity(25);
        product3.setStockWeight(2500);
        product3.setUnitPrice(43);
        product3.setSupplier(supplier1);

        product4.setCategory(Product.ProductCategory.PRICE_BY_QUANTITY);
        product4.setArrivalDate(LocalDate.of(2022, 10, 12));
        product4.setQuantity(10);
        product4.setStockWeight(200);
        product4.setUnitPrice(95);
        product4.setSupplier(supplier2);


        return args -> {
            if (employeeService.count() > 0) {
                log.info("Database already populated with employees. Skipping employee initialization.");
            } else {
                log.info("Loading data: " + employeeService.save(employee1));
                log.info("Loading data: " + employeeService.save(employee2));
            }
            if (supplierService.count() > 0) {
                log.info("Database already populated with suppliers. Skipping product initialization.");
            } else {
                log.info("Loading data: " + supplierService.save(supplier1));
                log.info("Loading data: " + supplierService.save(supplier2));
            }
            if (productService.count() > 0) {
                log.info("Database already populated with products. Skipping product initialization.");
            } else {
                log.info("Loading data: " + productService.save(product1));
                log.info("Loading data: " + productService.save(product2));
                log.info("Loading data: " + productService.save(product3));
                log.info("Loading data: " + productService.save(product4));
            }
        };
    }
}
