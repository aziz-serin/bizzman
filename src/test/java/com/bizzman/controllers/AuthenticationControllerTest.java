package com.bizzman.controllers;

import com.bizzman.BizzmanApplication;
import com.bizzman.dao.UserService;
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

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BizzmanApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
public class AuthenticationControllerTest {
    @LocalServerPort
    private int port;

    private WebTestClient client;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    public void shouldIntrospectReturn400GivenInvalidToken() {
        String invalidToken = "token";
        client.post().uri("/rest/authentication/introspect")
                .header("token", invalidToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void shouldIntrospectReturnClaims() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        client.post().uri("/rest/authentication/introspect")
                .header("token", token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void shouldAuthenticateFailGivenUserNotExists() {
        client.post().uri("/rest/authentication/authenticate")
                .header("username", "employee3")
                .header("password", "employee")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldAuthenticateFailGivenUsernameHeaderMissing() {
        client.post().uri("/rest/authentication/authenticate")
                .header("password", "employee")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldAuthenticateFailGivenPasswordHeaderMissing() {
        client.post().uri("/rest/authentication/authenticate")
                .header("username", "employee1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldAuthenticateSucceed() {
        client.post().uri("/rest/authentication/authenticate")
                .header("username", "admin")
                .header("password", "administrator")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void shouldRegisterFailGivenUserExists() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, String> json = Map.of(
                "username", "employee",
                "password", "password",
                "role", "user");
        client.post().uri("/rest/authentication/register")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldRegisterFailGivenBodyMissingInformation() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, String> json = Map.of(
                "username", "employee2",
                "password", "password");
        client.post().uri("/rest/authentication/register")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldRegisterFailGivenRoleDoesNotExist() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, String> json = Map.of(
                "username", "employee2",
                "password", "password",
                "role", "role");
        client.post().uri("/rest/authentication/register")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void shouldRegisterSucceed() {
        String token = TestTokenObtain.obtainTokenForAdmin(client);
        Map<String, String> json = Map.of(
                "username", "employee2",
                "password", "password",
                "role", "user");

        client.post().uri("/rest/authentication/register")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(json))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        assertThat(userService.usernameExists("employee2")).isTrue();
    }
}
