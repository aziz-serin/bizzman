package com.bizzman.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.*;

import com.sun.istack.NotNull;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "personalDetails")
    private PersonalDetails personalDetails;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "emergencyContactDetails")
    private List<EmergencyContact> emergencyContactDetails;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "name")
    @Size(max = 100, message = "Name cannot have more than 100 characters")
    @NotEmpty(message = "Name cannot be empty!")
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "workEmail")
    @NotEmpty(message = "Email cannot be empty!")
    private String workEmail;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "nationalInsurance")
    private String nationalInsurance;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "joiningDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate joiningDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "salary")
    @NotNull
    @Positive(message = "Salary must be positive")
    private double salary;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "otherExpenses")
    @NotNull
    @Positive(message = "Other expenses must be positive")
    private double otherExpenses;

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

    public double getOtherExpenses() {
        return otherExpenses;
    }

    public void setOtherExpenses(long other_expenses) {
        this.otherExpenses = other_expenses;
    }

    public List<EmergencyContact> getEmergencyContactDetails() {
        return emergencyContactDetails;
    }

    public void setEmergencyContactDetails(List<EmergencyContact> emergencyContactDetails) {
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
