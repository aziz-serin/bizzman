package com.bizzman.controllers;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.services.BusinessRelationshipService;
import com.bizzman.entities.BusinessRelationship;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
public class BusinessRelationshipControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient client;

    @Autowired
    private BusinessRelationshipService businessRelationshipService;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void shouldGetAllReturnAll() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> relationships = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();

        client.get().uri("/rest/businessRelationship/getAll")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(List.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).size()).isEqualTo(relationships.size())
                );
    }

    @Test
    public void shouldGetFailGivenIdDoesNotExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);

        client.get().uri("/rest/businessRelationship/get/999999")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    public void shouldGetSucceedGivenIdExists() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> relationships = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        long id = relationships.get(0).getId();
        client.get().uri("/rest/businessRelationship/get/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BusinessRelationship.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getId()).isEqualTo(id)
                );
    }

    @Test
    public void shouldDeleteAllFailGivenOrdersAreLinked() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        long count = businessRelationshipService.count();
        client.delete().uri("/rest/businessRelationship/deleteAll")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is5xxServerError();
        assertThat(businessRelationshipService.count()).isEqualTo(count);
    }

    @Test
    public void shouldDeleteFailGivenOrderIsLinked() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        List<BusinessRelationship> relationships = (List<BusinessRelationship>) businessRelationshipService.getAllRelationships();
        long id = relationships.get(0).getId();
        long count = businessRelationshipService.count();
        client.delete().uri("/rest/businessRelationship/delete/" + id)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is5xxServerError();
        assertThat(businessRelationshipService.count()).isEqualTo(count);
    }

    @Test
    public void shouldDeleteFailGivenRelationshipNotFound() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        long count = businessRelationshipService.count();
        client.delete().uri("/rest/businessRelationship/delete/" + 99999999)
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().is4xxClientError();
        assertThat(businessRelationshipService.count()).isEqualTo(count);
    }

    @Test
    public void shouldDeleteSucceedGivenOrderIsNotLinked() {
        BusinessRelationship businessRelationship = new BusinessRelationship();
        businessRelationship.setName("test");
        businessRelationship.setType(BusinessRelationship.Type.CUSTOMER);
        businessRelationship.setContacts(Map.of("name", "number"));
        BusinessRelationship businessRelationshipSaved = businessRelationshipService.save(businessRelationship);
        long count = businessRelationshipService.count();

        String token = TestTokenObtain.obtainTokenForAdmin(client);
        client.delete().uri("/rest/businessRelationship/delete/" + businessRelationshipSaved.getId())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk();
        assertThat(businessRelationshipService.count()).isEqualTo(count - 1);
    }

    @Test
    public void shouldCreateFailGivenMalformedContacts() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, String> json = Map.of(
                "name", "test",
                "type", "customer",
                "contacts", "contact"
        );
        long count = businessRelationshipService.count();
        client.post().uri("/rest/businessRelationship/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();

        assertThat(count).isEqualTo(businessRelationshipService.count());
    }

    @Test
    public void shouldCreateFailGivenMissingName() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "type", "customer",
                "contacts", Map.of("contact", "phone")
        );
        long count = businessRelationshipService.count();
        client.post().uri("/rest/businessRelationship/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();

        assertThat(count).isEqualTo(businessRelationshipService.count());
    }

    @Test
    public void shouldCreateFailGivenWrongType() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "name", "test",
                "type", "customer21",
                "contacts", Map.of("contact", "phone")
        );
        long count = businessRelationshipService.count();
        client.post().uri("/rest/businessRelationship/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();

        assertThat(count).isEqualTo(businessRelationshipService.count());
    }

    @Test
    public void shouldCreateFailGivenMissingType() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "name", "test",
                "contacts", Map.of("contact", "phone")
        );
        long count = businessRelationshipService.count();
        client.post().uri("/rest/businessRelationship/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();

        assertThat(count).isEqualTo(businessRelationshipService.count());
    }

    @Test
    public void shouldCreateSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, ?> json = Map.of(
                "name", "test",
                "type", "customer",
                "contacts", Map.of("contact", "phone")
        );
        long count = businessRelationshipService.count();
        client.post().uri("/rest/businessRelationship/create")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
        assertThat(count + 1).isEqualTo(businessRelationshipService.count());
    }

}
