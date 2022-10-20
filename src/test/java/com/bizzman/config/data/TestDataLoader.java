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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bizzman.dao.services.EmployeeService;
import com.bizzman.entities.EmergencyContactDetails;
import com.bizzman.entities.Employee;
import com.bizzman.entities.PersonalDetails;

@Configuration
@Profile("test")
public class TestDataLoader {
    private final static Logger log = LoggerFactory.getLogger(TestDataLoader.class);
    @Autowired
    EmployeeService employeeService;

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

        return args -> {
            if (employeeService.count() > 0) {
                log.info("Database already populated with employees. Skipping employee initialization.");
            } else {
                log.info("Loading data: " +employeeService.save(employee1));
                log.info("Loading data: " + employeeService.save(employee2));
            }
        };
    }
}