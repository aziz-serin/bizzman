package com.bizzman.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue
    private long id;

    private String name;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
}
