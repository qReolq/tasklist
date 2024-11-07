package qreol.project.tasklist.web.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import qreol.project.tasklist.config.TestContainersConfig;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.UserRepository;
import qreol.project.tasklist.service.AuthService;
import qreol.project.tasklist.service.UserService;
import qreol.project.tasklist.web.dto.auth.JwtRequest;

import java.time.LocalDateTime;

@Testcontainers
@ActiveProfiles("test")
@TestConfiguration(proxyBeanMethods = false)
@Import(TestContainersConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = String.format("http://localhost:%s/api/users", port);
        userRepository.deleteAll();
    }

    @Test
    void successGetUserByIdTest() {
        String rawPassword = "password";
        User user = new User(
                "name",
                "username",
                rawPassword,
                LocalDateTime.now()
        );
        long id = userService.create(user).getId();

        JwtRequest request = new JwtRequest(user.getUsername(), rawPassword);
        String token = authService.login(request).getAccessToken();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "Bearer " + token)
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", Matchers.equalTo(user.getName()))
                .body("username", Matchers.equalTo(user.getUsername()));
    }

    @Test
    void getUserByIdWithoutTokenTest() {
        String rawPassword = "password";
        User user = new User(
                "name",
                "username",
                rawPassword,
                LocalDateTime.now()
        );
        long id = userService.create(user).getId();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

}
