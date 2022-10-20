/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.entities;


import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

@Entity
@Table(name = "personal_details")
public class PersonalDetails {

    @Id
    @OneToOne
    private Employee employee;

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String passportNumber;

    private String phoneNumber;

    private String address;

    @NotNull
    private static long age;

    @NotNull
    @DateTimeFormat
    private String birthDate;

    public PersonalDetails() {}

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        LocalDate today_date = LocalDate.now();
        LocalDateTime personBirthDate = LocalDateTime.parse(birthDate);
        long age = java.time.temporal.ChronoUnit.YEARS.between( today_date , personBirthDate );
        this.birthDate = birthDate;
    }
}
