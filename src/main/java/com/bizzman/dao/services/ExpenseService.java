package com.bizzman.dao.services;

import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Employee;
import com.bizzman.entities.Expense;

import java.util.Optional;

public interface ExpenseService {

    Expense save(Expense expense);

    long count();

    void deleteById(Long id);

    void deleteAll();

    void deleteAll(Iterable<Expense> expenses);

    Optional<Expense> findById(Long id);

    Iterable<Expense> getAllExpenses();

    Iterable<Expense> getAllExpensesOfAnEmployee(Employee employee);

    Iterable<Expense> getAllExpensesSortedByDate(boolean isAscending);

    Iterable<Expense> getAllExpensesOfABusinessRelationship(BusinessRelationship businessRelationship);

    Iterable<Expense> getAllExpensesWithSameType(Expense.Type type);

    double getTotalExpenseCostOfAnEmployee(Employee employee);

    double getTotalExpenseCostOfABusinessRelationship(BusinessRelationship businessRelationship);

    Iterable<Expense> getExpensesWithSameBusinessRelationshipSortedByPrice(BusinessRelationship businessRelationship, boolean isAscending);

    double getTotalExpenseCostWithSameType(Expense.Type type);

    Iterable<Expense> getExpensesWithSameTypeSortedByPrice(Expense.Type type, boolean isAscending);

    double getAllExpenseCost();
}
