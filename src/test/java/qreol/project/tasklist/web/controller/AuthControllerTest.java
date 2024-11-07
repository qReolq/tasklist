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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import qreol.project.tasklist.config.TestContainersConfig;
import qreol.project.tasklist.domain.user.User;
import qreol.project.tasklist.repository.UserRepository;
import qreol.project.tasklist.web.dto.auth.JwtRequest;
import qreol.project.tasklist.web.dto.user.UserDTO;

import java.time.LocalDateTime;


@Testcontainers
@TestConfiguration(proxyBeanMethods = false)
@ActiveProfiles("test")
@Import(TestContainersConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = String.format("http://localhost:%s/api/auth", port);
        userRepository.deleteAll();
    }

    @Test
    void successRegisterTest() {
        UserDTO userDTO = new UserDTO(
                "name",
                "username",
                "pass",
                "pass"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/registration")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(
                        "id",
                        Matchers.notNullValue()
                ).body(
                        "username",
                        Matchers.equalTo(userDTO.getUsername())
                );
    }

    @Test
    void invalidPasswordConfirmationsTest() {
        UserDTO userDTO = new UserDTO(
                "name",
                "username",
                "pass",
                "invalidPassConfirm"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDTO)
                .when()
                .post("/registration")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "errors",
                        Matchers.aMapWithSize(1)
                ).body(
                        "message",
                        Matchers.equalTo("Validation failed")
                );
    }

    @Test
    void usernameTakenRegistrationsTest() {
        User user = new User(
                "name",
                "username",
                "pass",
                LocalDateTime.now()
        );
        UserDTO dto = new UserDTO(
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getPassword()
        );
        userRepository.save(user);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post("/registration")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "errors",
                        Matchers.aMapWithSize(1)
                ).body(
                        "message",
                        Matchers.equalTo("Validation failed")
                );
    }

    @Test
    void successLoginTest() {
        String password = "password";
        User user = new User(
                "name",
                "username",
                passwordEncoder.encode(password),
                LocalDateTime.now()
        );
        userRepository.save(user);

        JwtRequest request = new JwtRequest(
                user.getUsername(),
                password
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(
                        "accessToken",
                        Matchers.notNullValue()
                ).body(
                        "refreshToken",
                        Matchers.notNullValue()
                ).body(
                        "id",
                        Matchers.notNullValue()
                ).body(
                        "username",
                        Matchers.equalTo(request.getUsername())
                );

    }

    @Test
    void notExistUserLogin() {
        JwtRequest request = new JwtRequest(
                "username",
                "password"
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/login")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "message",
                        Matchers.equalTo("Authentication failed")
                );
    }


}