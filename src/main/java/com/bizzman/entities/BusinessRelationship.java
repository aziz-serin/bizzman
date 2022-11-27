package com.bizzman.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Map;

@Entity
@Table(name = "businessRelationship")
public class BusinessRelationship {

    public enum Type{
        CUSTOMER,
        SUPPLIER
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "type")
    Type type;

    @NotNull
    @Column(name = "name")
    @Size(max = 256, message = "Name should be less than 256 chars!")
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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
