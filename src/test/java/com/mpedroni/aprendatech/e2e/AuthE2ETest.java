package com.mpedroni.aprendatech.e2e;

import com.mpedroni.aprendatech.E2ETest;
import com.mpedroni.aprendatech.infra.providers.JwtProvider;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.BaseMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;

@E2ETest
public class AuthE2ETest {
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserJpaRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void returnsAJwtWhenAuthenticatingWithValidCredentials() {
        var aUsername = "johndoe";
        var aPassword = "password@123";

        givenAUser(aUsername, aPassword);

        given()
            .auth().none()
            .contentType("application/json")
            .body("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(aUsername, aPassword))
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .body("token", is(validJwt()));
    }

    @Test
    void throwsWhenAuthenticatingWithInvalidCredentials() {
        var aUsername = "johndoe";
        var aPassword = "password@123";

        givenAUser(aUsername, aPassword);

        given()
            .auth().none()
            .contentType("application/json")
            .body("""
            {
                "username": "%s",
                "password": "%s"
            }
            """.formatted(aUsername, "wrong password"))
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body("message", is("Username or password is incorrect."));
    }

    @Test
    void throwsWhenAuthenticatingWithNonExistentUsername() {
        var aUsername = "nonexistent";
        var aPassword = "password@123";

        given()
            .auth().none()
            .contentType("application/json")
            .body("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(aUsername, aPassword))
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body("message", is("Username or password is incorrect."));
    }

    void givenAUser(String aUsername, String aPassword) {
        var aName = "John Doe";
        var aEmail = "john@doe.com";
        var role = "STUDENT";

        given()
            .auth().none()
            .contentType("application/json")
            .body("""
                {
                    "name": "%s",
                    "username": "%s",
                    "email": "%s",
                    "password": "%s",
                    "role": "%s"
                }
                """.formatted(aName, aUsername, aEmail, aPassword, role))
        .when()
            .post("/users")
        .then()
            .statusCode(201);
    }

    Matcher<String> validJwt() {
        return new IsValidJwtMatcher();
    }

    class IsValidJwtMatcher extends BaseMatcher<String> {
        @Override
        public boolean matches(Object o) {
            return jwtProvider.verify((String) o) != null;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a valid JWT");
        }
    }
}
