package com.bizzman.controllers;

import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.dao.services.ExpenseService;
import com.bizzman.dao.services.OrderService;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Expense;
import com.bizzman.entities.Order;
import com.bizzman.entities.employee.Employee;
import com.bizzman.exceptions.custom.EntityConstructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.bizzman.exceptions.ExceptionMessages.BAD_REQUEST_BODY;
import static com.bizzman.exceptions.ExceptionMessages.REQUESTED_ENTITY_NOT_FOUND;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "rest/expense",  produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private BusinessRelationshipService businessRelationshipService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAll() {
        List<Expense> expenses = (List<Expense>) expenseService.getAllExpenses();
        return ResponseEntity.ok().body(expenses);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> get(@PathVariable @NotNull long id) {
        Optional<Expense> expense = expenseService.findById(id);
        if (expense.isPresent()) {
            return ResponseEntity.ok().body(expense.get());
        }
        logger.debug("Could not find the entity {}", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getAllExpensesOfEmployee/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllExpensesOfEmployee(@PathVariable @NotNull long id) {
        Optional<Employee> employee = employeeService.findEmployeeById(id);
        if (employee.isPresent()) {
            Iterable<Expense> expenses = expenseService.getAllExpensesOfAnEmployee(employee.get());
            return ResponseEntity.ok().body(expenses);
        }
        logger.debug("Could not find the entity {}", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getTotalExpenseOfEmployee/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getTotalExpenseOfEmployee(@PathVariable @NotNull long id) {
        Optional<Employee> employee = employeeService.findEmployeeById(id);
        if (employee.isPresent()) {
            double expense = expenseService.getTotalExpenseCostOfAnEmployee(employee.get());
            return ResponseEntity.ok().body(Map.of(
                    "expense", expense
            ));
        }
        logger.debug("Could not find the entity {}", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getAllExpensesOfBusinessRelationship/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllExpensesOfBusinessRelationship(@PathVariable @NotNull long id) {
        Optional<BusinessRelationship> relationship = businessRelationshipService.findById(id);
        if (relationship.isPresent()) {
            Iterable<Expense> expenses = expenseService.getAllExpensesOfABusinessRelationship(relationship.get());
            return ResponseEntity.ok().body(expenses);
        }
        logger.debug("Could not find the entity {}", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/geTotalExpenseOfBusinessRelationship/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getTotalExpenseOfBusinessRelationship(@PathVariable @NotNull long id) {
        Optional<BusinessRelationship> relationship = businessRelationshipService.findById(id);
        if (relationship.isPresent()) {
            double expense = expenseService.getTotalExpenseCostOfABusinessRelationship(relationship.get());
            return ResponseEntity.ok().body(Map.of(
                    "expense", expense
            ));
        }
        logger.debug("Could not find the entity {}", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getAllExpensesOfOrder/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllExpensesOfOrder(@PathVariable @NotNull long id) {
        Optional<Order> order = orderService.findById(id);
        if (order.isPresent()) {
            Iterable<Expense> expenses = expenseService.getAllExpensesOrder(order.get());
            return ResponseEntity.ok().body(expenses);
        }
        logger.debug("Could not find the entity {}", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getAllExpenseCost")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllExpenseCost() {
        return ResponseEntity.ok().body(Map.of(
           "expense", expenseService.getAllExpenseCost()
        ));
    }

    @GetMapping("/getAllExpensesSortedByDate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllExpensesSortedByDate(@RequestParam(required = false, name = "isAscending")
                                                            String isAscending) {
        boolean isAsc = Boolean.parseBoolean(isAscending);
        Iterable<Expense> expenses = expenseService.getAllExpensesSortedByDate(isAsc);
        return ResponseEntity.ok().body(expenses);
    }

    @PostMapping("/getAllExpenseWithSameType")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllExpensesWithSameType(@RequestBody @NotNull Map<String, ?> body) {
        Optional<Expense.Type> type = getType(body);
        if (type.isPresent()) {
            Iterable<Expense> expenses = expenseService.getAllExpensesWithSameType(type.get());
            return ResponseEntity.ok().body(expenses);
        }
        logger.debug("Could not find type {}", body.get("type"));
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @PostMapping("/getTotalCostWithSameType")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getTotalCostWithSameType(@RequestBody @NotNull Map<String, ?> body) {
        Optional<Expense.Type> type = getType(body);
        if (type.isPresent()) {
            double expense = expenseService.getTotalExpenseCostWithSameType(type.get());
            return ResponseEntity.ok().body(Map.of(
                    "expense", expense
            ));
        }
        logger.debug("Could not find type {}", body.get("type"));
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @PostMapping("/getAllSameTypeSortedByPrice")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllSameTypeSortedByPrice(@RequestBody @NotNull Map<String, ?> body,
                                                         @RequestParam(required = false, name = "isAscending") String isAscending) {
        Optional<Expense.Type> type = getType(body);
        boolean isAsc = Boolean.parseBoolean(isAscending);
        if (type.isPresent()) {
            Iterable<Expense> expenses = expenseService.getExpensesWithSameTypeSortedByPrice(type.get(), isAsc);
            return ResponseEntity.ok().body(expenses);
        }
        logger.debug("Could not find type {}", body.get("type"));
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @GetMapping("/getAllSameBusinessRelationshipSortedByPrice/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getAllSameBusinessRelationshipSortedByPrice(@PathVariable("id") @NotNull long id,
                                                                         @RequestParam(required = false, name = "isAscending") String isAscending) {
        Optional<BusinessRelationship> relationship = businessRelationshipService.findById(id);
        boolean isAsc = Boolean.parseBoolean(isAscending);
        if (relationship.isPresent()) {
            Iterable<Expense> expenses = expenseService
                    .getExpensesWithSameBusinessRelationshipSortedByPrice(relationship.get(), isAsc);
            return ResponseEntity.ok().body(expenses);
        }
        logger.debug("Could not find the entity {}", id);
        return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") @NotNull long id) {
        try {
            expenseService.deleteById(id);
            return ResponseEntity.ok().body("Deleted the requested resource");
        } catch (EmptyResultDataAccessException e) {
            logger.error("Expense with id {} does not exist", id);
            return ResponseEntity.status(404).body(REQUESTED_ENTITY_NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteAll() {
        expenseService.deleteAll();
        return ResponseEntity.ok().body("Deleted requested resources");
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody @NotNull Map<String, ?> body) {
        try {
            Optional<Expense.Type> type = getType(body);
            if (type.isEmpty()) {
                throw new EntityConstructionException("Could not construct the type");
            }
            Expense expense = null;
            switch (type.get()) {
                case EMPLOYEE_EXPENSE:
                    expense = createEmployeeExpense(body);
                    break;
                case ORDER:
                    expense = createOrderExpense(body);
                    break;
                case BUSINESS:
                    expense = createBusinessExpense(body);
                    break;
                case OTHER:
                    expense = createOtherExpense(body);
                    break;
            }
            if (expense == null) {
                throw new EntityConstructionException("Could not construct the entity!");
            }
            Expense saved = expenseService.save(expense);
            return ResponseEntity.ok().body("Created expense with id " + saved.getId());

        } catch (EntityConstructionException e) {
            logger.error("Required fields are missing/malformed");
            return ResponseEntity.unprocessableEntity().body(BAD_REQUEST_BODY);
        }
    }

    private Expense createEmployeeExpense(Map<String, ?> body) {
        long employeeId = Long.parseLong((String) body.get("employee"));
        Optional<Employee> employee = employeeService.findEmployeeById(employeeId);
        if (employee.isEmpty()) {
            throw new EntityConstructionException("Could not construct the entity!");
        }
        Expense expense = setMandatoryExpenseVariables(body, Expense.Type.EMPLOYEE_EXPENSE);
        expense.setEmployee(employee.get());
        return expense;

    }

    private Expense createBusinessExpense(Map<String, ?> body) {
        long businessId = Long.parseLong((String) body.get("business"));
        Optional<BusinessRelationship> relationship = businessRelationshipService.findById(businessId);
        if (relationship.isEmpty()) {
            throw new EntityConstructionException("Could not construct the entity!");
        }
        Expense expense = setMandatoryExpenseVariables(body, Expense.Type.BUSINESS);
        expense.setBusinessRelationship(relationship.get());
        return expense;
    }

    private Expense createOrderExpense(Map<String, ?> body) {
        long orderId = Long.parseLong((String) body.get("order"));
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty()) {
            throw new EntityConstructionException("Could not construct the entity!");
        }
        Expense expense = setMandatoryExpenseVariables(body, Expense.Type.ORDER);
        expense.setOrder(order.get());
        return expense;
    }

    private Expense createOtherExpense(Map<String, ?> body) {
        return setMandatoryExpenseVariables(body, Expense.Type.OTHER);
    }

    private Expense setMandatoryExpenseVariables(Map<String, ?> body, Expense.Type type) {
        try {
            double amount = Double.parseDouble((String) body.get("amount"));
            LocalDate date = LocalDate.parse((String) body.get("date"));
            Expense expense = new Expense();
            expense.setExpenseDate(date);
            expense.setType(type);
            expense.setAmount(amount);
            return expense;
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new EntityConstructionException("Could not construct the entity!");
        }
    }

    private Optional<Expense.Type> getType(Map<String, ?> body) {
        try {
            String type = (String) body.get("type");
            switch (type.toLowerCase()) {
                case "business":
                    return Optional.of(Expense.Type.BUSINESS);
                case "employee":
                    return Optional.of(Expense.Type.EMPLOYEE_EXPENSE);
                case "order":
                    return Optional.of(Expense.Type.ORDER);
                case "other":
                    return Optional.of(Expense.Type.OTHER);
                default:
                    return Optional.empty();
            }
        } catch (ClassCastException | NullPointerException e) {
            logger.debug("Could not parse the body, \n {}", body.toString());
            return Optional.empty();
        }
    }


}
