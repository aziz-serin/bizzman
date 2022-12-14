package com.bizzman.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Entity
@Table(name = "businessInformation")
public class BusinessInformation {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Business information cannot be empty!")
    @Size(max = 1000, message = "Description should not have more than 1000 characters!")
    @Column(name = "businessDescription", columnDefinition = "TEXT", length = 1000)
    private String businessDescription;

    @NotEmpty(message = "Business name cannot be empty")
    @Size(max = 300, message = "Business Name cannot be empty")
    @Column(name = "businessName")
    private String businessName;

    @NotNull
    @Column(name = "establishmentDate")
    private LocalDate establismentDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public LocalDate getEstablismentDate() {
        return establismentDate;
    }

    public void setEstablismentDate(LocalDate establismentDate) {
        this.establismentDate = establismentDate;
    }
}
