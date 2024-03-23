package com.mpedroni.aprendatech.e2e;

import com.mpedroni.aprendatech.E2ETest;
import org.junit.jupiter.api.Test;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.matchesPattern;

@E2ETest
class UserE2ETest {
    @Test
    void createsAStudent() {
        var name = "John Doe";
        var username = "johndoe";
        var email = "john@doe.com";
        var password = "password@123";
        var role = "STUDENT";

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
                """.formatted(name, username, email, password, role)
            )
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .header("Location", matchesPattern("/users/[\\d+]"));
    }
}
