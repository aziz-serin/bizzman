package com.bizzman.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "product")
public class Product {

    public enum ProductCategory{
        PRICE_BY_WEIGHT,
        PRICE_BY_QUANTITY
    }

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @NotNull
    @Column(name = "category")
    private ProductCategory category;

    @OneToOne
    @JoinColumn(name = "supplier_id")
    @NotNull
    private BusinessRelationship businessRelationship;

    @NotNull
    @Column(name = "entryUnitPrice")
    @Positive(message = "Selling unit price cannot be negative!")
    private double sellingUnitPrice;

    @NotNull
    @Column(name = "sellingUnitPrice")
    @Positive(message = "Entry unit price cannot be negative!")
    private double entryUnitPrice;

    @NotNull
    @Column(name = "weight")
    @Positive(message = "Stock weight cannot be negative")
    private double stockWeight;

    @NotNull
    @Column(name = "arrivalDate")
    private LocalDate arrivalDate;

    // As the name suggests, this attribute DOES NOT save the image, saves the path of the image in the filesystem
    @Column(name = "imagePath")
    @Size(max = 150, message = "File path cannot be longer than 150 characters")
    private String imagePath;

    @NotNull
    @Positive(message = "Quantity cannot be negative!")
    @Column(name = "quantity")
    private long quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public BusinessRelationship getSupplier() {
        return businessRelationship;
    }

    public void setSupplier(BusinessRelationship businessRelationship) {
        this.businessRelationship = businessRelationship;
    }

    public double getSellingUnitPrice() {
        return sellingUnitPrice;
    }

    public void setSellingUnitPrice(double unitPrice) {
        this.sellingUnitPrice = unitPrice;
    }

    public double getEntryUnitPrice() {
        return entryUnitPrice;
    }

    public void setEntryUnitPrice(double unitPrice) {
        this.entryUnitPrice = unitPrice;
    }

    public double getStockWeight() {
        return stockWeight;
    }

    public void setStockWeight(double stockWeight) {
        this.stockWeight = stockWeight;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

}
