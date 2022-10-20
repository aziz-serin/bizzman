/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table(name = "emergency_contact")
public class EmergencyContactDetails {

    private enum Relationship{
        FRIEND,
        IMMEDIATE_FAMILY,
        FAMILY,
        PARTNER,
        OTHER
    }

    @Id
    @NotNull
    @OneToOne
    private Employee employee;

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String phoneNumber;

    private Relationship relationship;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    public EmergencyContactDetails() {}

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Relationship getRelationship(){
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }
}
