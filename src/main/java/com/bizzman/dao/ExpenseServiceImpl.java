package com.bizzman.dao;

import com.bizzman.dao.repos.ExpenseRepository;
import com.bizzman.dao.services.ExpenseService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Employee;
import com.bizzman.entities.Expense;
import com.bizzman.entities.Order;
import com.bizzman.exceptions.custom.CustomNPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.bizzman.exceptions.ExceptionMessages.*;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;

    // ALWAYS RESET PRICE BEFORE USING THE ATTRIBUTE
    private double price;

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

    private <T> void throwExceptionIfNecessary(T object) throws CustomNPException{
        List<Expense> expenses = (List<Expense>) getAllExpenses();

        if (object.getClass().equals(Employee.class)) {
            List<Expense> filtered = expenses.stream()
                    .filter(e -> e.getEmployee() != null)
                    .collect(Collectors.toList());
            if (filtered.size() == 0) {
                throw new CustomNPException(EMPLOYEE_NOT_FOUND_EXCEPTION_MESSAGE, new NullPointerException().getCause());
            }
        } else if (object.getClass().equals(Order.class)) {
            List<Expense> filtered = expenses.stream()
                    .filter(e -> e.getOrder() != null)
                    .collect(Collectors.toList());
            if (filtered.size() == 0) {
                throw new CustomNPException(ORDER_NOT_FOUND_EXCEPTION_MESSAGE, new NullPointerException().getCause());
            }
        } else if (object.getClass().equals(BusinessRelationship.class)) {
            List<Expense> filtered = expenses.stream()
                    .filter(e -> e.getBusinessRelationship() != null)
                    .collect(Collectors.toList());
            if (filtered.size() == 0) {
                throw new CustomNPException(BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE, new NullPointerException().getCause());
            }
        }
    }

    @Override
    public Iterable<Expense> getAllExpensesOfAnEmployee(Employee employee) throws CustomNPException {
        //throwExceptionIfNecessary(employee);

        List<Expense> expenses = (List<Expense>) getAllExpenses();
        return expenses.stream()
                .filter(e -> e.getType().equals(Expense.Type.EMPLOYEE_EXPENSE))
                .filter(e -> Objects.equals(e.getEmployee().getId(), employee.getId()))
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Expense> getAllExpensesOrder(Order order) throws CustomNPException {
        throwExceptionIfNecessary(order);
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        return expenses.stream()
                .filter(e -> e.getType().equals(Expense.Type.ORDER))
                .filter(e -> Objects.equals(e.getOrder().getId(), order.getId()))
                .collect(Collectors.toList());
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
    public Iterable<Expense> getAllExpensesOfABusinessRelationship(BusinessRelationship businessRelationship) throws CustomNPException{
        throwExceptionIfNecessary(businessRelationship);

        List<Expense> expenses = (List<Expense>) getAllExpenses();
        return expenses.stream()
                .filter(e -> e.getType().equals(Expense.Type.BUSINESS))
                .filter(e -> Objects.equals(e.getBusinessRelationship().getId(), businessRelationship.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Expense> getAllExpensesWithSameType(Expense.Type type) {
        List<Expense> expenses = (List<Expense>) getAllExpenses();
        return expenses.stream()
                .filter(e -> e.getType().equals(type))
                .collect(Collectors.toList());
    }

    @Override
    public double getTotalExpenseCostOfAnEmployee(Employee employee) throws CustomNPException{
        throwExceptionIfNecessary(employee);

        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> employeeExpense = expenses.stream()
                .filter(e -> e.getType().equals(Expense.Type.EMPLOYEE_EXPENSE))
                .filter(e -> Objects.equals(e.getEmployee().getId(), employee.getId()))
                .collect(Collectors.toList());
        resetPrice();
        employeeExpense.forEach(e -> price += e.getAmount());
        return price;

    }

    @Override
    public double getTotalExpenseCostOfABusinessRelationship(BusinessRelationship businessRelationship) throws CustomNPException{
        throwExceptionIfNecessary(businessRelationship);

        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> businessRelationshipExpenses = expenses.stream()
                .filter(e -> e.getType().equals(Expense.Type.BUSINESS))
                .filter(e -> Objects.equals(e.getBusinessRelationship().getId(), businessRelationship.getId()))
                .collect(Collectors.toList());
        resetPrice();
        businessRelationshipExpenses.forEach(e -> price += e.getAmount());
        return price;
    }

    @Override
    public Iterable<Expense> getExpensesWithSameBusinessRelationshipSortedByPrice(BusinessRelationship businessRelationship, boolean isAscending) throws CustomNPException{
        throwExceptionIfNecessary(businessRelationship);

        List<Expense> expenses = (List<Expense>) getAllExpenses();
        List<Expense> sorted = expenses.stream()
                .filter(e -> e.getType().equals(Expense.Type.BUSINESS))
                .filter(e -> Objects.equals(e.getBusinessRelationship().getId(), businessRelationship.getId()))
                .sorted(Comparator.comparing(Expense::getAmount))
                .collect(Collectors.toList());
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
