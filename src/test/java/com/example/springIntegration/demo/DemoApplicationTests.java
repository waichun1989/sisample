package com.example.springIntegration.demo;

import com.example.api.DefaultApi;
import com.example.api.invoker.ApiClient;
import com.example.api.model.User;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static com.example.mock.api.DefaultApiMockServer.stubUsersIdGet200;
import static com.example.mock.api.DefaultApiMockServer.usersIdGet200ResponseSample1;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@SpringBootTest
class DemoApplicationTests {


    private WireMockServer wireMockServer;

    @BeforeEach
    void setUp() {
        // Start WireMock on a random available port
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();

        // Tell static WireMock DSL where to send requests
        configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGetUserById() {
        // 1. Stub the response using the generated OpenAPI WireMock helper
        wireMockServer.stubFor(stubUsersIdGet200("123", usersIdGet200ResponseSample1()));

        // 2. Make the request to WireMock (your client under test should point here)
        // Example using a simple HTTP client:
        String baseUrl = "http://localhost:" + wireMockServer.port();
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(baseUrl);
        ResponseEntity<User> responseEntity = new DefaultApi(apiClient).usersIdGetWithHttpInfo(123);

        // 3. Assert the result
        Assertions.assertEquals("name", Objects.requireNonNull(responseEntity.getBody()).getName());
    }
}
