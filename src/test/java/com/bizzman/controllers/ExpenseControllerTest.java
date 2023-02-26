package com.bizzman.controllers;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.dao.services.ExpenseService;
import com.bizzman.dao.services.OrderService;
import com.bizzman.dao.services.employee.EmployeeService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Expense;
import com.bizzman.entities.Order;
import com.bizzman.entities.employee.Employee;
import com.bizzman.utils.TestTokenObtain;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
public class ExpenseControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient client;

    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private BusinessRelationshipService businessRelationshipService;
    @Autowired
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void shouldGetAllReturnAll() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/getAll")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> assertThat(response.getResponseBody())
                        .hasSameSizeAs(expenseService.getAllExpenses()));
    }

    @Test
    public void shouldGetFailGivenExpenseDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/get/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Expense> expenses = (List<Expense>) expenseService.getAllExpenses();
        long expenseId = expenses.get(0).getId();

        client.get().uri("/rest/expense/get/" + expenseId)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response ->
                        AssertionsForClassTypes.assertThat(Objects.requireNonNull(response.getResponseBody()).get("id"))
                                .isEqualTo((int) expenseId));
    }

    @Test
    public void shouldGetAllExpenseOfEmployeeFailGivenEmployeeDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/getAllExpensesOfEmployee/" + 999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetAllExpenseOfEmployeeSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        long id = employees.get(0).getId();

        client.get().uri("/rest/expense/getAllExpensesOfEmployee/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService.getAllExpensesOfAnEmployee(employees.get(0)));
                });
    }

    @Test
    public void shouldGetTotalExpenseOfEmployeeFailGivenEmployeeDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/getTotalExpenseOfEmployee/" + 999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetTotalExpenseOfEmployeeSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        long id = employees.get(0).getId();

        client.get().uri("/rest/expense/getTotalExpenseOfEmployee/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody().get("expense"))
                            .isEqualTo(expenseService.getTotalExpenseCostOfAnEmployee(employees.get(0)));
                });
    }

    @Test
    public void shouldGetAllExpenseOfRelationshipFailGivenRelationshipDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);


        client.get().uri("/rest/expense/getAllExpensesOfBusinessRelationship/" + 999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetAllExpenseOfRelationshipSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships
                = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        long id = businessRelationships.get(0).getId();

        client.get().uri("/rest/expense/getAllExpensesOfBusinessRelationship/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService.getAllExpensesOfABusinessRelationship(businessRelationships.get(0)));
                });
    }

    @Test
    public void shouldGetTotalExpenseOfRelationshipFailGivenRelationshipDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/geTotalExpenseOfBusinessRelationship/" + 999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetTotalExpenseOfRelationshipSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships
                = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        long id = businessRelationships.get(0).getId();

        client.get().uri("/rest/expense/geTotalExpenseOfBusinessRelationship/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody().get("expense"))
                            .isEqualTo(expenseService.getTotalExpenseCostOfABusinessRelationship(businessRelationships.get(0)));
                });
    }

    @Test
    public void shouldGetAllExpenseOfOrderFailGivenOrderDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/getAllExpensesOfOrder/" + 999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetAllExpensesOfOrderSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        long id = orders.get(0).getId();

        client.get().uri("/rest/expense/getAllExpensesOfOrder/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService.getAllExpensesOrder(orders.get(0)));
                });
    }

    @Test
    public void shouldGetAllExpenseCostSucceed(){
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/getAllExpenseCost")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody().get("expense"))
                            .isEqualTo(expenseService.getAllExpenseCost());
                });
    }

    @Test
    public void shouldGetAllExpensesSortedReturnDescending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/getAllExpensesSortedByDate")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService.getAllExpensesSortedByDate(false));
                });
    }

    @Test
    public void shouldGetAllExpensesSortedReturnAscending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/expense/getAllExpensesSortedByDate?isAscending=true")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService.getAllExpensesSortedByDate(false));
                });
    }

    @Test
    public void shouldGetAllExpensesWithSameTypeFailGivenMalformedType() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "test"
        );

        client.post().uri("/rest/expense/getAllExpenseWithSameType")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetAllExpensesWithSameTypeSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "business"
        );

        client.post().uri("/rest/expense/getAllExpenseWithSameType")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService.getAllExpensesWithSameType(Expense.Type.BUSINESS));
                });
    }

    @Test
    public void shouldGetTotalCostWithSameTypeFailGivenMalformedType() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "test"
        );

        client.post().uri("/rest/expense/getTotalCostWithSameType")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetTotalCostWithSameTypeSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "business"
        );

        client.post().uri("/rest/expense/getTotalCostWithSameType")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody().get("expense"))
                            .isEqualTo(expenseService.getTotalExpenseCostWithSameType(Expense.Type.BUSINESS));
                });
    }

    @Test
    public void shouldGetAllSameTypeSortedByPriceSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "business"
        );

        client.post().uri("/rest/expense/getAllSameTypeSortedByPrice?isAscending=true")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService.getExpensesWithSameTypeSortedByPrice(Expense.Type.BUSINESS, true));
                });
    }

    @Test
    public void shouldGetAllBusinessSortedByPriceSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships
                = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        long id = businessRelationships.get(0).getId();
        String uri = String.format("/rest/expense/getAllSameBusinessRelationshipSortedByPrice/%s?isAscending=true", id);

        client.get().uri(uri)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(expenseService
                                    .getExpensesWithSameBusinessRelationshipSortedByPrice(businessRelationships.get(0),
                                            true));
                });
    }

    @Test
    public void shouldDeleteFailGivenExpenseDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        long count = expenseService.count();

        client.delete().uri("/rest/expense/delete/" + 999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();

        assertThat(count).isEqualTo(expenseService.count());
    }

    @Test
    public void shouldDeleteSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Expense> expenses = (List<Expense>) expenseService.getAllExpenses();
        long count = expenseService.count();

        client.delete().uri("/rest/expense/delete/" + expenses.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(count - 1).isEqualTo(expenseService.count());
    }

    @Test
    public void shouldCreateForEmployeeSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Employee> employees = (List<Employee>) employeeService.findAllEmployees();
        Map<String, ?> json = Map.of(
                "type", "employee",
                "employee", String.valueOf(employees.get(0).getId()),
                "amount", "45",
                "date", LocalDate.now().toString()
        );
        long count = expenseService.count();

        client.post().uri("/rest/expense/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(count + 1).isEqualTo(expenseService.count());
    }

    @Test
    public void shouldCreateForBusinessSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> relationships
                = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        Map<String, ?> json = Map.of(
                "type", "business",
                "business", String.valueOf(relationships.get(0).getId()),
                "amount", "45",
                "date", LocalDate.now().toString()
        );
        long count = expenseService.count();

        client.post().uri("/rest/expense/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(count + 1).isEqualTo(expenseService.count());
    }

    @Test
    public void shouldCreateForOrderSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        Map<String, ?> json = Map.of(
                "type", "order",
                "order", String.valueOf(orders.get(0).getId()),
                "amount", "45",
                "date", LocalDate.now().toString()
        );
        long count = expenseService.count();

        client.post().uri("/rest/expense/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(count + 1).isEqualTo(expenseService.count());
    }

    @Test
    public void shouldCreateForOtherSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "other",
                "amount", "45",
                "date", LocalDate.now().toString()
        );
        long count = expenseService.count();

        client.post().uri("/rest/expense/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(count + 1).isEqualTo(expenseService.count());
    }


}
