package com.bizzman.dao;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.ExpenseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// These tests will need modification if the TestDataLoader class's product initialisation order or values are modified.

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class)
@DirtiesContext
@ActiveProfiles("test")
public class ExpenseServiceTest {

    // Test any custom method in expenseService
    @Autowired
    ExpenseService expenseService;

    @Test
    void getAllExpensesOfAnEmployeeReturnsTrueExpenseAmount() {

    }

    @Test
    void getAllExpensesSortedByDateReturnsTrulySortedList() {
    }

    @Test
    void getAllExpensesOfABusinessRelationshipReturnsTrueExpenseAmount() {
    }

    @Test
    void getAllExpensesOfOrderReturnsTrueOrder() {

    }

    @Test
    void getAllExpensesWithSameTypeReturnsTrueList() {
    }

    @Test
    void getTotalExpenseCostOfAnEmployeeReturnsTrueCost() {
    }

    @Test
    void getTotalExpenseCostOfABusinessRelationshipReturnsTrueList() {
    }

    @Test
    void getExpensesWithSameBusinessRelationshipSortedByPriceReturnsTrueSortedList() {
    }

    @Test
    void getTotalExpenseCostWithSameTypeReturnsTrueExpenseAmount() {
    }

    @Test
    void getExpensesWithSameTypeSortedByPriceReturnsTrueList() {
    }

    @Test
    void getAllExpenseCostReturnsTrueAmount() {
    }
}