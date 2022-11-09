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

import com.bizzman.dao.services.OrderService;
import com.bizzman.dao.services.ProductService;
import com.bizzman.dao.services.BusinessRelationshipService;
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
    BusinessRelationshipService businessRelationshipService;
    @Autowired
    OrderService orderService;


    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private BusinessRelationship businessRelationship1;
    private BusinessRelationship businessRelationship2;
    private Employee employee1;
    private PersonalDetails personalDetails1;
    private EmergencyContactDetails emergencyContactDetail1;
    private Employee employee2;
    private PersonalDetails personalDetails2;
    private EmergencyContactDetails emergencyContactDetail2;

    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;

    private List<Employee> loadEmployee() {
        employee1 = new Employee();
        personalDetails1 = new PersonalDetails();
        emergencyContactDetail1 = new EmergencyContactDetails();
        employee2 = new Employee();
        personalDetails2 = new PersonalDetails();
        emergencyContactDetail2 = new EmergencyContactDetails();

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

        return List.of(employee1, employee2);
    }

    private List<Product> loadProduct() {
        product1 = new Product();
        product2 = new Product();
        product3 = new Product();
        product4 = new Product();


        product1.setCategory(Product.ProductCategory.PRICE_BY_WEIGHT);
        product1.setArrivalDate(LocalDate.of(2022, 10, 9));
        product1.setQuantity(5);
        product1.setStockWeight(100.8);
        product1.setUnitPrice(40);
        product1.setSupplier(businessRelationship1);

        product2.setCategory(Product.ProductCategory.PRICE_BY_QUANTITY);
        product2.setArrivalDate(LocalDate.of(2022, 10, 17));
        product2.setQuantity(100);
        product2.setStockWeight(21.2);
        product2.setUnitPrice(5);
        product2.setSupplier(businessRelationship2);

        product3.setCategory(Product.ProductCategory.PRICE_BY_WEIGHT);
        product3.setArrivalDate(LocalDate.of(2022, 10, 5));
        product3.setQuantity(25);
        product3.setStockWeight(2500);
        product3.setUnitPrice(43);
        product3.setSupplier(businessRelationship1);

        product4.setCategory(Product.ProductCategory.PRICE_BY_QUANTITY);
        product4.setArrivalDate(LocalDate.of(2022, 10, 12));
        product4.setQuantity(10);
        product4.setStockWeight(200);
        product4.setUnitPrice(95);
        product4.setSupplier(businessRelationship2);

        return List.of(product1, product2, product3, product4);
    }

    private List<BusinessRelationship> loadBusinessRelationship() {
        businessRelationship1 = new BusinessRelationship();
        businessRelationship2 = new BusinessRelationship();

        businessRelationship1.setContacts(Map.of("Ahmed", "738219321", "Melissa", "39382948490"));
        businessRelationship1.setName("Alu");
        businessRelationship1.setType(BusinessRelationship.Type.SUPPLIER);

        businessRelationship2.setContacts(Map.of("Jen", "738439321", "Ben", "39382948490"));
        businessRelationship2.setType(BusinessRelationship.Type.SUPPLIER);
        businessRelationship2.setName("Acc");

        return List.of(businessRelationship1, businessRelationship2);
    }

    private List<Order> loadOrders(){
        order1 = new Order();
        order2 = new Order();
        order3 = new Order();
        order4 = new Order();

        order1.setType(Order.Type.INCOMING);
        order1.setProducts(List.of(product1, product2));
        order1.setPlacingDate(LocalDate.of(2022, 10, 17));
        order1.setArrivalDate(LocalDate.of(2022, 10, 22));
        order1.setBusinessRelationship(businessRelationship1);

        order2.setType(Order.Type.INCOMING);
        order2.setProducts(List.of(product1, product3));
        order2.setPlacingDate(LocalDate.of(2022, 9, 17));
        order2.setArrivalDate(LocalDate.of(2022, 9, 22));
        order2.setBusinessRelationship(businessRelationship1);

        order3.setType(Order.Type.OUTGOING);
        order3.setProducts(List.of(product2, product4));
        order3.setPlacingDate(LocalDate.of(2022, 11, 17));
        order3.setArrivalDate(LocalDate.of(2022, 11, 22));
        order3.setBusinessRelationship(businessRelationship2);

        order4.setType(Order.Type.OUTGOING);
        order4.setProducts(List.of(product3, product4));
        order4.setPlacingDate(LocalDate.of(2022, 12, 17));
        order4.setArrivalDate(LocalDate.of(2022, 12, 22));
        order4.setBusinessRelationship(businessRelationship2);

        return List.of(order1, order2, order3, order4);
    }

    @Bean
    CommandLineRunner initDatabase(){

        List<Employee> employees = loadEmployee();
        List<BusinessRelationship> businessRelationships = loadBusinessRelationship();
        List<Product> products = loadProduct();
        List<Order> orders = loadOrders();

        return args -> {
            if (employeeService.count() > 0) {
                log.info("Database already populated with employees. Skipping employee initialization.");
            } else {
                for (Employee employee : employees) {
                    log.info("Loading data: " + employeeService.save(employee));
                }
            }
            if (businessRelationshipService.count() > 0) {
                log.info("Database already populated with suppliers. Skipping product initialization.");
            } else {
                for (BusinessRelationship businessRelationship : businessRelationships) {
                    log.info("Loading data: " + businessRelationshipService.save(businessRelationship));
                }
            }
            if (productService.count() > 0) {
                log.info("Database already populated with products. Skipping product initialization.");
            } else {
                for (Product product : products) {
                    log.info("Loading data: " + productService.save(product));
                }
            }
            if (orderService.count() > 0) {
                log.info("Database already populated with orders. Skipping product initialization.");
            } else {
                for (Order order : orders) {
                    log.info("Loading data: " + orderService.save(order));
                }
            }
        };
    }
}
