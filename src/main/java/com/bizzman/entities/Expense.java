package com.bizzman.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expense")
public class Expense {

    public enum Type{
        BUSINESS,
        EMPLOYEE_EXPENSE,
        ORDER,
        OTHER
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "expenseAmount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "employees")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "expenseOrder")
    private Order order;

    @NotNull
    @Column(name = "expenseType")
    private Type type;

    @NotNull
    @JoinColumn(name = "expenseDate")
    private LocalDate expenseDate;

    @ManyToOne
    @JoinColumn(name = "businessRelationship")
    private BusinessRelationship businessRelationship;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate date){
        this.expenseDate = date;
    }

    public BusinessRelationship getBusinessRelationship() {
        return businessRelationship;
    }

    public void setBusinessRelationship(BusinessRelationship businessRelationship) {
        this.businessRelationship = businessRelationship;
    }
}
