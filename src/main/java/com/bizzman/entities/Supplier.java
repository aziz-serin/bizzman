package com.bizzman.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "supplier")
public class Supplier {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "name")
    private String name;

    //Should be in the form of name, phone number
    @ElementCollection
    @Column(name = "contacts")
    private Map<String, String> contacts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }
}
