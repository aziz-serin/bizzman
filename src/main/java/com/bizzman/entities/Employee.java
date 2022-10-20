package com.bizzman.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.*;

import com.sun.istack.NotNull;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pd_id", referencedColumnName = "id")
    private PersonalDetails personalDetails;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "e_c", referencedColumnName = "id")
    private EmergencyContactDetails emergencyContactDetails;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "ni")
    private String nationalInsurance;

    @Column(name = "joining data")
    @DateTimeFormat
    @NotNull
    private LocalDate joiningDate;

    @Column(name = "salary")
    private double salary;

    @Column(name = "other_expenses")
    private double other_expenses;

    public Employee() {
        this.joiningDate = LocalDate.now();
    }

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

    public EmergencyContactDetails getEmergencyContactDetails() {
        return emergencyContactDetails;
    }

    public void setEmergencyContactDetails(EmergencyContactDetails emergencyContactDetails) {
        this.emergencyContactDetails = emergencyContactDetails;
    }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
}
