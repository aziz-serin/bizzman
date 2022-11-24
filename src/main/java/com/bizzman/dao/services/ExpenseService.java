package com.bizzman.dao.services;

import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Employee;
import com.bizzman.entities.Expense;
import com.bizzman.entities.Order;

import java.util.Optional;

public interface ExpenseService {

    Expense save(Expense expense);

    long count();

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<Expense> expenses);

    Optional<Expense> findById(Long id);

    Iterable<Expense> getAllExpenses();

    Iterable<Expense> getAllExpensesOfAnEmployee(Employee employee) throws Exception;

    // no need for methods to calculate expenses with orders, because this can be done using OrderService.java
    Iterable<Expense> getAllExpensesOrder(Order order) throws Exception;

    Iterable<Expense> getAllExpensesSortedByDate(boolean isAscending);

    Iterable<Expense> getAllExpensesOfABusinessRelationship(BusinessRelationship businessRelationship) throws Exception;

    Iterable<Expense> getAllExpensesWithSameType(Expense.Type type);

    double getTotalExpenseCostOfAnEmployee(Employee employee) throws Exception;

    double getTotalExpenseCostOfABusinessRelationship(BusinessRelationship businessRelationship) throws Exception;

    Iterable<Expense> getExpensesWithSameBusinessRelationshipSortedByPrice(BusinessRelationship businessRelationship, boolean isAscending) throws Exception;

    double getTotalExpenseCostWithSameType(Expense.Type type);

    Iterable<Expense> getExpensesWithSameTypeSortedByPrice(Expense.Type type, boolean isAscending);

    double getAllExpenseCost();
}
