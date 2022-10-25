package com.bizzman.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

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
    @NotNull
    private String name;

    @Column(name = "ni")
    private String nationalInsurance;

    @Column(name = "joiningDate")
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

    public List<EmergencyContactDetails> getEmergencyContactDetails() {
        return emergencyContactDetails;
    }

    public void setEmergencyContactDetails(List<EmergencyContactDetails> emergencyContactDetails) {
        this.emergencyContactDetails = emergencyContactDetails;
    }
    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
}
