package com.bizzman.utils;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;
import java.util.Objects;

public class TestTokenObtain {
    private static String token;

    public static String obtainTokenForAdmin(WebTestClient client) {
        client.post().uri("/rest/authentication/authenticate")
                .header("username", "admin")
                .header("password", "administrator")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    token = (String) Objects.requireNonNull(result.getResponseBody()).get("token");
                });
        return token;
    }

    public static String obtainTokenForEmployee(WebTestClient client) {
        client.post().uri("/rest/authentication/authenticate")
                .header("username", "employee")
                .header("password", "employee")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(Map.class)
                .consumeWith(result -> {
                    token = (String) Objects.requireNonNull(result.getResponseBody()).get("token");
                });
        return token;
    }
}
