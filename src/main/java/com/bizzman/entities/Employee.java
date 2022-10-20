package com.bizzman.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.*;

import com.sun.istack.NotNull;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @OneToOne
    private PersonalDetails personalDetails;

    @OneToOne
    @NotNull
    private EmergencyContactDetails emergencyContactDetails;

    @NotNull
    private String name;

    private String nationalInsurance;

    @DateTimeFormat
    @NotNull
    private String joiningDate;

    private double salary;

    private double other_expenses;

    public Employee() {
        Date date = new Date();
        joiningDate = date.toString();
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getNationalInsurance() {
        return nationalInsurance;
    }

    public void setNationalInsurance(String nationalInsurance) {
        this.nationalInsurance = nationalInsurance;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getOther_expenses() {
        return other_expenses;
    }

    public void setOther_expenses(long other_expenses) {
        this.other_expenses = other_expenses;
    }

    public EmergencyContactDetails getEmergencyContactDetails() {
        return emergencyContactDetails;
    }

    public void setEmergencyContactDetails(EmergencyContactDetails emergencyContactDetails) {
        this.emergencyContactDetails = emergencyContactDetails;
    }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
}
