package com.bizzman.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.*;

import com.sun.istack.NotNull;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @NotNull
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "personalDetails")
    private PersonalDetails personalDetails;

    @NotNull
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "emergencyContact")
    private List<EmergencyContactDetails> emergencyContactDetails;

    @Column(name = "name")
    @Size(max = 100, message = "Name cannot have more than 100 characters")
    @NotEmpty(message = "Name cannot be empty!")
    private String name;

    @Column(name = "workEmail")
    @NotEmpty(message = "Email cannot be empty!")
    private String workEmail;

    @Column(name = "ni")
    private String nationalInsurance;

    @Column(name = "joiningDate")
    @DateTimeFormat
    @NotNull
    private LocalDate joiningDate;

    @Column(name = "salary")
    @NotNull
    @Positive(message = "Salary must be positive")
    private double salary;

    @Column(name = "other_expenses")
    @Positive(message = "Other expenses must be positive")
    private double other_expenses;

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
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

    public List<EmergencyContactDetails> getEmergencyContactDetails() {
        return emergencyContactDetails;
    }

    public void setEmergencyContactDetails(List<EmergencyContactDetails> emergencyContactDetails) {
        this.emergencyContactDetails = emergencyContactDetails;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
