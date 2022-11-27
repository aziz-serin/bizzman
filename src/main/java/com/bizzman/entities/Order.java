package com.bizzman.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bizzOrder")
public class Order {

    public enum Type{
        OUTGOING,
        INCOMING
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type")
    Type type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "businessRelationship")
    BusinessRelationship businessRelationship;

    @NotNull
    @ManyToMany
    @JoinColumn(name = "products")
    List<Product> products;

    @NotNull
    @Column(name = "placingDate")
    @Past(message = "Placing date cannot be in the future")
    LocalDate placingDate;

    @NotNull
    @Column(name = "arrivalDate")
    @Future(message = "Arrival date cannot be in the past!")
    LocalDate arrivalDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BusinessRelationship getBusinessRelationship() {
        return businessRelationship;
    }

    public void setBusinessRelationship(BusinessRelationship businessRelationship) {
        this.businessRelationship = businessRelationship;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public LocalDate getPlacingDate() {
        return placingDate;
    }

    public void setPlacingDate(LocalDate date) {
        this.placingDate = date;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
