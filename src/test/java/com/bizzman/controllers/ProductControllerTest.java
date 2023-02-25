package com.bizzman.controllers;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.dao.services.ProductService;
import com.bizzman.entities.BusinessRelationship;
import com.bizzman.entities.Product;
import com.bizzman.utils.TestTokenObtain;
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
public class ProductControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient client;

    @Autowired
    private ProductService productService;

    @Autowired
    private BusinessRelationshipService businessRelationshipService;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void shouldGetAllSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Product> product = (List<Product>) productService.getAllProducts();

        client.get().uri("/rest/product/getAll")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(product.size())
                );
    }

    @Test
    public void shouldGetFailGivenProductDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        client.get().uri("/rest/product/get/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Product> products = (List<Product>) productService.getAllProducts();
        Product product = products.get(0);

        client.get().uri("/rest/product/get/" + product.getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Product.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getId())
                                .isEqualTo(product.getId()));
    }

    @Test
    public void shouldGetAllWeightSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        double weight = productService.getTotalWeight();

        client.get().uri("/rest/product/getTotalWeight")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, Double> map = response.getResponseBody();
                    assert map != null;
                    assertThat(map).containsKey("weight");
                    assertThat(map.get("weight")).isEqualTo(weight);
                });
    }

    @Test
    public void shouldGetRecentEntryDateSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        String date = productService.getMostRecentDateOfItemEntry().toString();

        client.get().uri("/rest/product/getRecentEntryDate")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, String> map = response.getResponseBody();
                    assert map != null;
                    assertThat(map).containsKey("date");
                    assertThat(map.get("date")).isEqualTo(date);
                });
    }

    @Test
    public void shouldGetOldestEntryDateSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        String date = productService.getOldestDateOfItemEntry().toString();

        client.get().uri("/rest/product/getOldestEntryDate")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, String> map = response.getResponseBody();
                    assert map != null;
                    assertThat(map).containsKey("date");
                    assertThat(map.get("date")).isEqualTo(date);
                });
    }

    @Test
    public void shouldGetSuppliersSucceed() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);
        List<BusinessRelationship> businessRelationships =
                (List<BusinessRelationship>) productService.getAllProductSuppliers();

        client.get().uri("/rest/product/getSuppliers")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    List<?> relationships = response.getResponseBody();
                    assertThat(relationships).hasSameSizeAs(businessRelationships);
                });
    }

    @Test
    public void shouldGetProductTotalSellingPriceFailGivenProductNotExist() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);

        client.get().uri("/rest/product/getProductTotalSellingPrice/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetProductTotalSellingPriceSucceed() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);
        List<Product> products = (List<Product>) productService.getAllProducts();
        long id = products.get(0).getId();

        client.get().uri("/rest/product/getProductTotalSellingPrice/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, Double> map = response.getResponseBody();
                    assert map != null;
                    assertThat(map).containsKey("price");
                    assertThat(map.get("price")).isEqualTo(productService.getProductTotalSellingPrice(products.get(0)));
                });
    }

    @Test
    public void shouldGetProductTotalEntryPriceFailGivenProductNotExist() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);

        client.get().uri("/rest/product/getProductTotalEntryPrice/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetProductTotalEntryPriceSucceed() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);
        List<Product> products = (List<Product>) productService.getAllProducts();
        long id = products.get(0).getId();

        client.get().uri("/rest/product/getProductTotalEntryPrice/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, Double> map = response.getResponseBody();
                    assert map != null;
                    assertThat(map).containsKey("price");
                    assertThat(map.get("price")).isEqualTo(productService.getProductTotalEntryPrice(products.get(0)));
                });
    }

    @Test
    public void shouldGetExpectedProfitFailGivenProductNotExist() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);

        client.get().uri("/rest/product/getExpectedProfit/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetExpectedProfitSucceed() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);
        List<Product> products = (List<Product>) productService.getAllProducts();
        long id = products.get(0).getId();

        client.get().uri("/rest/product/getExpectedProfit/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, Double> map = response.getResponseBody();
                    assert map != null;
                    assertThat(map).containsKey("price");
                    assertThat(map.get("price")).isEqualTo(productService.getExpectedProfitFromProduct(products.get(0)));
                });
    }

    @Test
    public void shouldGetAllFromSameSupplierFailGivenSupplierDoesntExist() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);

        client.get().uri("/rest/product/getAllFromSameSupplier/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetAllFromSameSupplierSucceed() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);
        List<BusinessRelationship> relationships
                = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();

        client.get().uri("/rest/product/getAllFromSameSupplier/" + relationships.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(productService.getAllProductsFromSameSupplier(relationships.get(0).getId()));
                });
    }

    @Test
    public void shouldGetAllWeightFromSameSupplierFailGivenSupplierDoesntExist() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);

        client.get().uri("/rest/product/getAllWeightFromSameSupplier/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetAllWeightFromSameSupplierSucceed() {
        String token = TestTokenObtain.obtainTokenForEmployee(client);
        List<BusinessRelationship> relationships
                = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();

        client.get().uri("/rest/product/getAllWeightFromSameSupplier/" + relationships.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    Map<String, Double> map = response.getResponseBody();
                    assert map != null;
                    assertThat(map).containsKey("weight");
                    assertThat(map.get("weight")).isEqualTo(productService.getWeightOfAllProductsFromSameSupplier(relationships.get(0).getId()));
                });
    }

    @Test
    public void shouldGetProductByCategoryFailGivenInvalidRequestBody() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "category", "test"
        );

        client.post().uri("/rest/product/getProductByCategory")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldGetProductByCategorySucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "category", "price_by_weight"
        );

        client.post().uri("/rest/product/getProductByCategory")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(productService.getAllProductsFromSameCategory(Product.ProductCategory.PRICE_BY_WEIGHT));
                });
    }

    @Test
    public void shouldGetTotalPriceOfCategoryFailGivenInvalidRequestBody() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "category", "test"
        );

        client.post().uri("/rest/product/getTotalPriceOfCategory")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldGetTotalPriceOfCategorySucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "category", "price_by_quantity"
        );

        client.post().uri("/rest/product/getTotalPriceOfCategory")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .containsKey("price")
                            .containsValue(productService.getTotalPriceOfCategory(Product.ProductCategory.PRICE_BY_QUANTITY));
                });
    }

    @Test
    public void shouldGetTotalWeightOfCategoryFailGivenInvalidRequestBody() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "category", "test"
        );

        client.post().uri("/rest/product/getTotalWeightOfCategory")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldGetTotalWeightOfCategorySucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "category", "price_by_quantity"
        );

        client.post().uri("/rest/product/getTotalWeightOfCategory")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .containsKey("weight")
                            .containsValue(productService.getTotalWeightOfCategory(Product.ProductCategory.PRICE_BY_QUANTITY));
                });
    }

    @Test
    public void shouldGetTotalPriceSucceedForSellingPrice() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/product/getTotalPrice?isSelling=true")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .containsKey("price")
                            .containsValue(productService.getTotalPrice(true));
                });
    }

    @Test
    public void shouldGetTotalPriceSucceedForEntryPrice() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/product/getTotalPrice")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Map.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .containsKey("price")
                            .containsValue(productService.getTotalPrice(false));
                });
    }

    @Test
    public void shouldGetAllSortedByWeightSucceedForAscending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/product/getAllSortedByWeight?isAscending=true")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(productService.getProductListSortedByWeight(true));
                });
    }

    @Test
    public void shouldGetAllSortedByWeightSucceedForDescending() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/product/getAllSortedByWeight")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody())
                            .hasSameSizeAs(productService.getProductListSortedByWeight(false));
                });
    }

    @Test
    public void shouldDeleteFailGivenProductDoesntExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        client.delete().uri("/rest/product/delete/" + 999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldDeleteFailGivenExistingRelationshipExists() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<Product> products = (List<Product>) productService.getAllProducts();

        client.delete().uri("/rest/product/delete/" + products.get(0).getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is5xxServerError();
    }

    @Test
    public void shouldDeleteSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        Product product = new Product();
        product.setEntryUnitPrice(90);
        product.setImagePath("test");
        product.setQuantity(4);
        product.setArrivalDate(LocalDate.now());
        product.setSellingUnitPrice(54);
        product.setStockWeight(5);
        product.setCategory(Product.ProductCategory.PRICE_BY_QUANTITY);
        product.setSupplier(businessRelationships.get(0));

        long id = productService.save(product).getId();
        long count = productService.count();

        client.delete().uri("/rest/product/delete/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(count).isGreaterThan(productService.count());
    }

    @Test
    public void shouldCreateFailGivenNonExistingCategory() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "entryUnitPrice", "45",
                "sellingUnitPrice", "65",
                "quantity", "100",
                "stockWeight", "6",
                "imagePath", "file",
                "category", "test"
        );

        client.post().uri("/rest/product/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldCreateFailGivenOneMissingPrimitive() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "entryUnitPrice", "45",
                "sellingUnitPrice", "65",
                "quantity", "100",
                "imagePath", "file",
                "category", "price_by_weight"
        );

        client.post().uri("/rest/product/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldCreateFailGivenMissingSupplier() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "entryUnitPrice", "45",
                "sellingUnitPrice", "65",
                "quantity", "100",
                "stockWeight", "6",
                "imagePath", "file",
                "arrivalDate", LocalDate.now().toString(),
                "category", "price_by_weight"
        );

        client.post().uri("/rest/product/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldCreateSucceedWithImagePath() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        long id = businessRelationships.get(0).getId();
        Map<String, ?> json = Map.of(
                "entryUnitPrice", "45",
                "sellingUnitPrice", "65",
                "quantity", "100",
                "stockWeight", "6",
                "imagePath", "file",
                "arrivalDate", LocalDate.now().toString(),
                "category", "price_by_weight",
                "businessRelationship", String.valueOf(id)
        );

        long beforeCreation = productService.count();

        client.post().uri("/rest/product/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(beforeCreation).isEqualTo(productService.count() - 1);
    }

    @Test
    public void shouldCreateSucceedWithoutImagePath() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> businessRelationships = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        long id = businessRelationships.get(0).getId();
        Map<String, ?> json = Map.of(
                "entryUnitPrice", "45",
                "sellingUnitPrice", "65",
                "quantity", "100",
                "stockWeight", "6",
                "arrivalDate", LocalDate.now().toString(),
                "category", "price_by_weight",
                "businessRelationship", String.valueOf(id)
        );

        long beforeCreation = productService.count();

        client.post().uri("/rest/product/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .exchange()
                .expectStatus()
                .isOk();

        assertThat(beforeCreation).isEqualTo(productService.count() - 1);
    }


}
