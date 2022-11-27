/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.entities;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

@Entity
@Table(name = "personaldetails")
public class PersonalDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "passportNumber")
    @NotEmpty
    @Size(max = 44, min = 11, message = "Passport number must have a valid length between 11 and 44!")
    private String passportNumber;

    @NotEmpty
    @Size(max = 11, min = 10, message = "Phone number must have a valid length!")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "address")
    @Size(max=200, message = "address cannot be longer than 200 characters!")
    private String address;

    @Column(name = "age")
    @Positive(message = "Age cannot be a negative number!")
    @Max(150) // If you have an employee older than 150, feel free to change it
    @NotNull
    private int age;

    @Column(name = "personalEmail")
    @NotEmpty(message = "Email cannot be empty!")
    private String personalEmail;

    @Column(name = "birthDate")
    @NotNull
    @DateTimeFormat
    @Past(message = "Birth Date cannot be in the future!")
    private LocalDate birthDate;

    public PersonalDetails() {}

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

    public int getAge() {
        return age;
    }

    private void setAge(int age) {
        this.age = age;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.age = (int) ChronoUnit.YEARS.between(LocalDate.now(), birthDate);
        this.birthDate = birthDate;
    }
}
