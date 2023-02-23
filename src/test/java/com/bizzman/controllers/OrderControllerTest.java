package com.bizzman.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.dao.services.OrderService;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Order;
import com.bizzman.entities.Product;
import com.bizzman.utils.TestTokenObtain;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
public class OrderControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient client;

    @Autowired
    private BusinessRelationshipService businessRelationshipService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void shouldGetAllSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Order> orders = (List<Order>) orderService.getAllOrders();

        client.get().uri("/rest/order/getAll")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(orders.size())
                );
    }

    @Test
    public void shouldGetFailGivenOrderNotExists() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        client.get().uri("/rest/order/get/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetGivenOrderExists() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        long order = orders.get(0).getId();
        client.get().uri("/rest/order/get/" + order)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Order.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getId()).isEqualTo(order)
                );
    }

    @Test
    public void shouldGetOrderWithSameTypeFailGivenMissingRequestBody() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of();

        client.post().uri("/rest/order/getAllWithSameType")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();

    }
    @Test
    public void shouldGetOrderWithSameTypeFailGivenTypeDoesNotExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "test"
        );

        client.post().uri("/rest/order/getAllWithSameType")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();

    }

    @Test
    public void shouldGetOrderWithSameTypeSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "incoming"
        );
        List<Order> orders = (List<Order>) orderService.getAllOrdersWithSameType(Order.Type.INCOMING);

        client.post().uri("/rest/order/getAllWithSameType")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(orders.size());
                });
    }

    @Test
    public void shouldGetOrderPriceFailGivenOrderNotExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/order/getPrice/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetOrderPriceSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Order> orders = (List<Order>) orderService.getAllOrders();
        Double price = orderService.getOrderPrice(orders.get(0));

        client.get().uri("/rest/order/getPrice/" + orders.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).get("price")).isEqualTo(price)
                );
    }

    @Test
    public void shouldGetAllSortedByArrivalFailGivenEmptyBody() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of();

        client.post().uri("/rest/order/getAllSortedByArrival")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldGetAllSortedByArrivalFailGivenBodyIsNotBoolean() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "isAscending", "test"
        );

        client.post().uri("/rest/order/getAllSortedByArrival")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldGetAllSortedByArrivalSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "isAscending", true
        );
        List<Order> orders = (List<Order>) orderService.getAllOrdersSortedByArrivalDate(true);

        client.post().uri("/rest/order/getAllSortedByArrival")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(orders.size());
                });
    }

    @Test
    public void shouldGetAllForBusinessRelationshipFailGivenRelationshipNotExists() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        client.get().uri("/rest/order/getAllForBusinessRelationship/9999999")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldGetAllForBusinessRelationshipSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship>  relationships = (List<BusinessRelationship>) businessRelationshipService
                .getAllRelationships();
        List<Order> orders = (List<Order>) orderService.getAllOrdersForBusinessRelationship(relationships.get(0).getId());

        client.get().uri("/rest/order/getAllForBusinessRelationship/" + relationships.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(orders.size());
                });
    }

    @Test
    public void shouldGetAllWithTypeSortedByArrivalSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "incoming",
                "isAscending", true
        );
        List<Order> orders = (List<Order>) orderService
                .getAllOrdersSameTypeSortedByArrivalDate(Order.Type.INCOMING, true);

        client.post().uri("/rest/order/getAllWithTypeSortedByArrival")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(orders.size());
                });
    }

    @Test
    public void shouldGetAllWithTypeSortedByPriceSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "incoming",
                "isAscending", true
        );
        List<Order> orders = (List<Order>) orderService
                .getAllOrdersSameTypeSortedByPrice(Order.Type.INCOMING, true);

        client.post().uri("/rest/order/getAllWithTypeSortedByPrice")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(orders.size());
                });
    }

    @Test
    public void shouldDeleteFailGivenOrderNotExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Order> ordersBefore = (List<Order>) orderService.getAllOrders();

        client.delete().uri("rest/order/deleteById/99999999999")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is4xxClientError();

        List<Order> ordersAfter = (List<Order>) orderService.getAllOrders();
        assertThat(ordersBefore.size()).isEqualTo(ordersAfter.size());
    }

    @Test
    public void shouldDeleteFailGivenItIsReferenced() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Order> ordersBefore = (List<Order>) orderService.getAllOrders();

        client.delete().uri("rest/order/deleteById/" + ordersBefore.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is5xxServerError();

        List<Order> ordersAfter = (List<Order>) orderService.getAllOrders();
        assertThat(ordersBefore.size()).isEqualTo(ordersAfter.size());
    }

    @Test
    public void shouldDeleteSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        long id = createOrder();
        List<Order> ordersBefore = (List<Order>) orderService.getAllOrders();

        client.delete().uri("rest/order/deleteById/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk();

        List<Order> ordersAfter = (List<Order>) orderService.getAllOrders();
        assertThat(ordersBefore.size() - 1).isEqualTo(ordersAfter.size());
    }

    @Test
    public void shouldCreateFailGivenTypeNotExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService
                .getAllRelationships();
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        Map<String, ?> json = Map.of(
                "type", "test",
                "businessRelationship", String.valueOf(businessRelationships.get(0).getId()),
                "products", productIds,
                "arrivalTime", LocalDate.now().toString(),
                "placingDate", LocalDate.now().toString()
        );

        client.post().uri("/rest/order/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldCreateFailGivenBusinessRelationshipNotExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        Map<String, ?> json = Map.of(
                "type", "incoming",
                "businessRelationship", "4390218430921",
                "products", productIds,
                "arrivalTime", LocalDate.now().toString(),
                "placingDate", LocalDate.now().toString()
        );

        client.post().uri("/rest/order/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldCreateFailGivenNotAllProductsExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService
                .getAllRelationships();
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        productIds.add(49090L);

        Map<String, ?> json = Map.of(
                "type", "incoming",
                "businessRelationship", String.valueOf(businessRelationships.get(0).getId()),
                "products", productIds,
                "arrivalTime", LocalDate.now().toString(),
                "placingDate", LocalDate.now().toString()
        );

        client.post().uri("/rest/order/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldCreateFailGivenArrivalTimeMalformed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService
                .getAllRelationships();
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        Map<String, ?> json = Map.of(
                "type", "incoming",
                "businessRelationship", String.valueOf(businessRelationships.get(0).getId()),
                "products", productIds,
                "arrivalTime", "someTime",
                "placingDate", LocalDate.now().toString()
        );

        client.post().uri("/rest/order/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldCreateFailGivenPlacingDateMalformed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService
                .getAllRelationships();
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        productIds.add(49090L);

        Map<String, ?> json = Map.of(
                "type", "incoming",
                "businessRelationship", String.valueOf(businessRelationships.get(0).getId()),
                "products", productIds,
                "arrivalTime", LocalDate.now().toString(),
                "placingDate", "someTime"
        );

        client.post().uri("/rest/order/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldCreateSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService
                .getAllRelationships();
        List<Product> products = (List<Product>) productService.getAllProducts();
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());
        List<Order> ordersBefore = (List<Order>) orderService.getAllOrders();

        Map<String, ?> json = Map.of(
                "type", "incoming",
                "businessRelationship", String.valueOf(businessRelationships.get(0).getId()),
                "products", productIds,
                "arrivalTime", LocalDate.now().toString(),
                "placingDate", LocalDate.now().toString()
        );

        client.post().uri("/rest/order/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        List<Order> ordersAfter = (List<Order>) orderService.getAllOrders();
        assertThat(ordersBefore.size() + 1).isEqualTo(ordersAfter.size());
    }

    private long createOrder() {
        Order order = new Order();
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService
                .getAllRelationships();

        order.setProducts(List.of());
        order.setType(Order.Type.INCOMING);
        order.setPlacingDate(LocalDate.now());
        order.setArrivalDate(LocalDate.now());
        order.setBusinessRelationship(businessRelationships.get(0));

        return orderService.save(order).getId();
    }
}
