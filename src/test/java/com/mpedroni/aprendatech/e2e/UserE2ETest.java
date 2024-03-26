package com.mpedroni.aprendatech.e2e;

import com.mpedroni.aprendatech.E2ETest;
import com.mpedroni.aprendatech.infra.providers.JwtProvider;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;

@E2ETest
class UserE2ETest {
    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    JwtProvider jwt;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }


    @Test
    void createsAStudent() {
        var aName = "John Doe";
        var aUsername = "johndoe";
        var anEmail = "john@doe.com";
        var aPassword = "password@123";
        var aRole = "STUDENT";

        given()
            .auth()
            .none()
            .contentType("application/json")
            .body("""
                {
                    "name": "%s",
                    "username": "%s",
                    "email": "%s",
                    "password": "%s",
                    "role": "%s"
                }
                """.formatted(aName, aUsername, anEmail, aPassword, aRole)
            )
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .header("Location", matchesPattern("/users/[\\d+]"));
    }

    @Test
    void returnsTheSearchedUserWhenTheAuthenticatedUserIsAnAdmin() {
        var admin = new UserJpaEntity(
            "Administrator",
            "admin",
            "admin@username.com",
            "password@123",
            "ADMIN"
        );

        var student = new UserJpaEntity(
            "John Doe",
            "johndoe",
            "john@doe.com",
            "password@123",
            "STUDENT"
        );

        userRepository.saveAll(List.of(admin, student));

        var token = jwt.generate(admin.getUsername(), admin.getRole());

        given()
            .contentType("application/json")
            .header("Authorization", "Bearer %s".formatted(token))
        .when()
            .get("/users/%s".formatted(student.getUsername()))
        .then()
            .statusCode(200)
            .body("id", is(anything()))
            .body("name", is(student.getName()))
            .body("email", is(student.getEmail()))
            .body("role", is(student.getRole()));
    }
}
