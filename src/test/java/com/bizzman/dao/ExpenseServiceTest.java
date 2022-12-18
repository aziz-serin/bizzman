package com.bizzman.dao;

import static com.bizzman.exceptions.ExceptionMessages.BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.bizzman.exceptions.ExceptionMessages.EMPLOYEE_NOT_FOUND_EXCEPTION_MESSAGE;
import static com.bizzman.exceptions.ExceptionMessages.ORDER_NOT_FOUND_EXCEPTION_MESSAGE;

import static org.assertj.core.api.Assertions.assertThat;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.ExpenseService;
import com.bizzman.entities.Expense;
import com.bizzman.exceptions.custom.CustomNPException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;


// These tests will need modification if the TestDataLoader class's product initialisation order or values are modified.

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class ExpenseServiceTest {

    // Test any custom method in expenseService
    @Autowired
    ExpenseService expenseService;

    private List<Expense> getExpenses(){
        return (List<Expense>) expenseService.getAllExpenses();
    }

    @Test
    public void getAllExpensesOfAnEmployeeReturnsTrueExpenses() {
        List<Expense> expenses = getExpenses();
        try {
            List<Expense> employeeExpense = (List<Expense>) expenseService.getAllExpensesOfAnEmployee(expenses.get(0).getEmployee());

            assertThat(employeeExpense.get(0).getId()).isEqualTo(expenses.get(0).getId());
            assertThat(employeeExpense.get(0).getType()).isEqualTo(Expense.Type.EMPLOYEE_EXPENSE);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(CustomNPException.class);
            assertThat(e.getMessage()).isEqualTo(EMPLOYEE_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void getAllExpensesSortedByDateReturnsTrulySortedList() {
        List<Expense> expenses = getExpenses();
        List<Expense> sortedAscending = (List<Expense>) expenseService.getAllExpensesSortedByDate(true);
        List<Expense> sortedDescending = (List<Expense>) expenseService.getAllExpensesSortedByDate(false);

        assertThat(sortedAscending.get(0).getId()).isEqualTo(expenses.get(2).getId());

        assertThat(sortedDescending.get(0).getId()).isEqualTo(expenses.get(1).getId());
    }

    @Test
    public void getAllExpensesOfABusinessRelationshipReturnsTrueExpenses() {
        List<Expense> expenses = getExpenses();
        try {
            List<Expense> businessExpense = (List<Expense>) expenseService.getAllExpensesOfABusinessRelationship(expenses.get(2).getBusinessRelationship());

            assertThat(businessExpense.get(0).getId()).isEqualTo(expenses.get(2).getId());
            assertThat(businessExpense.get(0).getType()).isEqualTo(Expense.Type.BUSINESS);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(CustomNPException.class);
            assertThat(e.getMessage()).isEqualTo(BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void getAllExpensesOfOrderReturnsTrueExpenses() {
        List<Expense> expenses = getExpenses();
        try {
            List<Expense> orderExpense = (List<Expense>) expenseService.getAllExpensesOrder(expenses.get(1).getOrder());

            assertThat(orderExpense.get(0).getId()).isEqualTo(expenses.get(1).getId());
            assertThat(orderExpense.get(0).getType()).isEqualTo(Expense.Type.ORDER);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(NullPointerException.class);
            assertThat(e.getMessage()).isEqualTo(ORDER_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void getAllExpensesWithSameTypeReturnsTrueList() {
        List<Expense> expenses = getExpenses();
        List<Expense> expensesWithSameType = (List<Expense>) expenseService.getAllExpensesWithSameType(Expense.Type.OTHER);

        assertThat(expensesWithSameType.size()).isEqualTo(1);
        assertThat(expensesWithSameType.get(0).getId()).isEqualTo(expenses.get(3).getId());
        assertThat(expensesWithSameType.get(0).getType()).isEqualTo(Expense.Type.OTHER);
    }

    @Test
    public void getTotalExpenseCostOfAnEmployeeReturnsTrueCost() {
        List<Expense> expenses = getExpenses();

        try {
            double employeeExpense = expenseService.getTotalExpenseCostOfAnEmployee(expenses.get(0).getEmployee());

            assertThat(employeeExpense).isEqualTo(34_600);

        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(NullPointerException.class);
            assertThat(e.getMessage()).isEqualTo(EMPLOYEE_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void getTotalExpenseCostOfABusinessRelationshipReturnsTrueList() {
        List<Expense> expenses = getExpenses();

        try {
            double businessExpense = expenseService.getTotalExpenseCostOfABusinessRelationship(expenses.get(2).getBusinessRelationship());

            assertThat(businessExpense).isEqualTo(54000.9);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(NullPointerException.class);
            assertThat(e.getMessage()).isEqualTo(BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void getExpensesWithSameBusinessRelationshipSortedByPriceReturnsTrueSortedList() {
        List<Expense> expenses = getExpenses();

        try {
            List<Expense> businessExpenses = (List<Expense>) expenseService
                    .getExpensesWithSameBusinessRelationshipSortedByPrice(expenses.get(2).getBusinessRelationship(), true);

            assertThat(businessExpenses.get(0).getId()).isEqualTo(expenses.get(2).getId());

            businessExpenses = (List<Expense>) expenseService
                    .getExpensesWithSameBusinessRelationshipSortedByPrice(expenses.get(2).getBusinessRelationship(), false);

            assertThat(businessExpenses.get(0).getId()).isEqualTo(expenses.get(2).getId());
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(NullPointerException.class);
            assertThat(e.getMessage()).isEqualTo(BUSINESS_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    @Test
    public void getTotalExpenseAmountWithSameTypeReturnsTrueExpenseAmount() {
        double expenseAmount = expenseService.getTotalExpenseCostWithSameType(Expense.Type.OTHER);

        assertThat(expenseAmount).isEqualTo(90750.0);

    }

    @Test
    public void getExpensesWithSameTypeSortedByPriceReturnsTrueList() {
        List<Expense> expenses = getExpenses();
        List<Expense> businessExpenses = (List<Expense>) expenseService
                .getExpensesWithSameTypeSortedByPrice(Expense.Type.BUSINESS, true);

        assertThat(businessExpenses.get(0).getType()).isEqualTo(Expense.Type.BUSINESS);
        assertThat(businessExpenses.get(0).getId()).isEqualTo(expenses.get(2).getId());

        businessExpenses = (List<Expense>) expenseService
                .getExpensesWithSameTypeSortedByPrice(Expense.Type.BUSINESS, false);
        Collections.reverse(businessExpenses);
        assertThat(businessExpenses.get(0).getType()).isEqualTo(Expense.Type.BUSINESS);
        assertThat(businessExpenses.get(0).getId()).isEqualTo(expenses.get(2).getId());
    }

    @Test
    public void getAllExpenseAmountReturnsTrueAmount() {
        double allExpenseAmount = expenseService.getAllExpenseCost();

        assertThat(allExpenseAmount).isEqualTo(183882.9);
    }
}