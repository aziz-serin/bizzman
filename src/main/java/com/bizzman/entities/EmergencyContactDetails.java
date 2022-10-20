/*
 * Copyright 2022 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */

package com.bizzman.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

@Entity
@Table(name = "emergencycontact")
public class EmergencyContactDetails {

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

    @Column(name = "phoneNumber")
    @NotNull
    private String phoneNumber;

    @Column(name = "relationship")
    private Relationship relationship;

    @Column(name = "emName")
    @NotNull
    private String name;

    public EmergencyContactDetails() {}

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
