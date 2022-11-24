package com.bizzman.dao;

import com.bizzman.dao.repos.ExpenseRepository;
import com.bizzman.dao.services.ExpenseService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Employee;
import com.bizzman.entities.Expense;
import com.bizzman.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    // ALWAYS RESET PRICE BEFORE USING THE ATTRIBUTE
    private double price;

    private static final String BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE = "Business Relationship does not exist for this expense!";

    private static final String EMPLOYEE_NOT_FOUND_EXCEPTION_MESSAGE = "Employee does not exist for this expense!";

    private static final String ORDER_NOT_FOUND_EXCEPTION_MESSAGE = "Order does not exist for this expense!";

    private void resetPrice(){
        this.price = 0;
    }

    @Override
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public long count() {
        return expenseRepository.count();
    }

    @Override
    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        expenseRepository.deleteAll();
    }

    @Override
    public void deleteAll(Iterable<Expense> expenses) {
        expenseRepository.deleteAll(expenses);
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public Iterable<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public Iterable<Expense> getAllExpensesOfAnEmployee(Employee employee) throws Exception{
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> filteredExpenses =  expenses.stream()
                .filter(e -> e.getEmployee().equals(employee))
                .collect(Collectors.toList());
        if (filteredExpenses.size() == 0) {
            throw new AttributeNotFoundException(EMPLOYEE_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return filteredExpenses;
    }

    @Override
    public Iterable<Expense> getAllExpensesOrder(Order order) throws Exception {
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> filteredExpenses =  expenses.stream()
                .filter(e -> e.getOrder().equals(order))
                .collect(Collectors.toList());
        if (filteredExpenses.size() == 0) {
            throw new AttributeNotFoundException(ORDER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return filteredExpenses;
    }

    @Override
    public Iterable<Expense> getAllExpensesSortedByDate(boolean isAscending) {
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> sorted = expenses.stream()
                .sorted(Comparator.comparing(Expense::getExpenseDate))
                .collect(Collectors.toList());
        if (!isAscending) {
            Collections.reverse(sorted);
        }
        return sorted;
    }

    @Override
    public Iterable<Expense> getAllExpensesOfABusinessRelationship(BusinessRelationship businessRelationship) throws Exception{
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> filteredExpenses = expenses.stream()
                .filter(e -> e.getBusinessRelationship() != null)
                .filter(e -> e.getBusinessRelationship().equals(businessRelationship))
                .collect(Collectors.toList());
        if (expenses.size() == 0) {
            throw new AttributeNotFoundException(BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        return filteredExpenses;
    }

    @Override
    public Iterable<Expense> getAllExpensesWithSameType(Expense.Type type) {
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        return expenses.stream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public double getTotalExpenseCostOfAnEmployee(Employee employee) throws Exception{
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> employeeExpense = expenses.stream()
                .filter(e -> e.getEmployee().equals(employee))
                .collect(Collectors.toList());
        if (employeeExpense.size() == 0) {
            throw new AttributeNotFoundException(EMPLOYEE_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        resetPrice();
        employeeExpense.forEach(e -> price += e.getAmount());
        return price;

    }

    @Override
    public double getTotalExpenseCostOfABusinessRelationship(BusinessRelationship businessRelationship) throws Exception{
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> businessRelationshipExpenses = expenses.stream()
                .filter(e -> e.getBusinessRelationship().equals(businessRelationship))
                .collect(Collectors.toList());
        if (businessRelationshipExpenses.size() == 0){
            throw new AttributeNotFoundException(BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        resetPrice();
        businessRelationshipExpenses.forEach(e -> price += e.getAmount());
        return price;
    }

    @Override
    public Iterable<Expense> getExpensesWithSameBusinessRelationshipSortedByPrice(BusinessRelationship businessRelationship, boolean isAscending) throws Exception{
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> sorted = expenses.stream()
                .filter(e -> e.getBusinessRelationship().equals(businessRelationship))
                .sorted(Comparator.comparing(Expense::getAmount))
                .collect(Collectors.toList());
        if (sorted.size() == 0){
            throw new AttributeNotFoundException(BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE);
        }
        if (!isAscending) {
            Collections.reverse(sorted);
        }
        return sorted;
    }

    @Override
    public double getTotalExpenseCostWithSameType(Expense.Type type) {
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> expensesSameType = expenses.stream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
        resetPrice();
        expensesSameType.forEach(e -> price += e.getAmount());
        return price;
    }

    @Override
    public Iterable<Expense> getExpensesWithSameTypeSortedByPrice(Expense.Type type, boolean isAscending) {
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> sorted = expenses.stream()
                .filter(e -> e.getType().equals(type))
                .sorted(Comparator.comparing(Expense::getAmount))
                .collect(Collectors.toList());
        if (!isAscending) {
            Collections.reverse(sorted);
        }
        return sorted;
    }

    @Override
    public double getAllExpenseCost() {
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        resetPrice();
        expenses.forEach(expense -> price += expense.getAmount());
        return price;
    }
}
