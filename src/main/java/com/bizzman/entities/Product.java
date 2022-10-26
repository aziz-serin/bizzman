package com.bizzman.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @NotNull
    @Column(name = "category")
    private String category;

    @NotNull
    @OneToOne
    @JoinColumn(name = "supplier")
    private Supplier supplier;

    @NotNull
    @Column(name = "unitPrice")
    private long unitPrice;

    @NotNull
    @Column(name = "weight")
    private double stockWeight;

    @NotNull
    @Column(name = "arrivalDate")
    private LocalDate arrivalDate;

    // As the name suggests, this attribute DOES NOT save the image, saves the path of the image in the filesystem
    @Column(name = "imagePath")
    private String imagePath;

    @NotNull
    @Column(name = "quantity")
    private long quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
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
