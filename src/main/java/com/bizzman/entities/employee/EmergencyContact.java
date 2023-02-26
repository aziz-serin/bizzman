/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.entities.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.bizzman.entities.employee.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

@Entity
@Table(name = "emergencyContact")
public class EmergencyContact {

    public enum Relationship{
        FRIEND,
        IMMEDIATE_FAMILY,
        FAMILY,
        PARTNER,
        OTHER
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "phoneNumber")
    @NotEmpty
    @Size(max = 11, min = 10, message = "Phone number must have a valid length!")
    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    @Column(name = "relationship")
    private Relationship relationship;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "emName")
    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot have more than 100 characters")
    @NotNull
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
