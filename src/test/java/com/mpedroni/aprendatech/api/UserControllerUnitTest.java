package com.mpedroni.aprendatech.api;

import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.infra.config.security.SecurityConfig;
import com.mpedroni.aprendatech.infra.users.api.UserController;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
public class UserControllerUnitTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setupRestAssured() {
        RestAssuredMockMvc.mockMvc(mvc);
    }

    @Test
    void createsAStudent() {
        var request = UserRequestBuilder.builder().post();

        var name = request.name();
        var username = request.username();
        var email = request.email();
        var password = request.password();
        var role = request.role();

        when(createUserUseCase.execute(name, username, email, password, role))
            .thenReturn(
                UserJpaEntity.from(1L, request.name(), request.username(), request.email(), request.password(), request.role()
            )
        );

        given()
            .auth().none()
            .contentType("application/json")
            .body(request.build())
        .when()
            .post("/users")
        .then()
            .statusCode(201)
            .header("Location", matchesPattern("/users/[\\d+]"));
    }

    @Test
    void throwsAnErrorWhenRequiredFieldsAreBlank() {
        var request = UserRequestBuilder.builder().blank().post();

        given()
            .auth().none()
            .contentType("application/json")
            .body(request.build())
        .when()
            .post("/users")
        .then()
            .statusCode(400)
            .body("errors", hasItems(
                    "Name is required.",
                    "Username is required.",
                    "Email is required.",
                    "Password is required.",
                    "Role is required."
            ));
    }

    @Test
    void throwsAnErrorWhenUsernameHasInvalidLength() {
        var aUsernameWithMoreThan20Characters = "a".repeat(21);
        var request = UserRequestBuilder.builder().withUsername(aUsernameWithMoreThan20Characters).post();

        given()
            .auth().none()
            .contentType("application/json")
            .body(request.build())
        .when()
            .post("/users")
        .then()
            .statusCode(400)
            .body("errors", hasItem("Username must have at most 20 characters."));
    }

    @Test
    void throwsAnErrorWhenUsernameHasInvalidCharacters() {
        var aUsernameWithInvalidCharacters = "J0hn Do3@";
        var request = UserRequestBuilder.builder().withUsername(aUsernameWithInvalidCharacters).post();

        given()
            .auth().none()
            .contentType("application/json")
            .body(request.build())
        .when()
            .post("/users")
        .then()
            .statusCode(400)
            .body("errors", hasItem("Username must have only lower case characters, without numbers or blank spaces."));
    }

    @Test
    void throwsAnErrorWhenEmailIsInvalid() {
        var anInvalidEmail = "john@doe";
        var request = UserRequestBuilder.builder().withEmail(anInvalidEmail).post();

        given()
            .auth().none()
            .contentType("application/json")
            .body(request.build())
        .when()
            .post("/users")
        .then()
            .statusCode(400)
            .body("errors", hasItem("Email must be a well-formed email address."));
    }

    @Test
    void throwsAnErrorWhenRoleIsInvalid() {
        var anInvalidRole = "INVALID";
        var request = UserRequestBuilder.builder().withRole(anInvalidRole).post();

        given()
            .auth().none()
            .contentType("application/json")
            .body(request.build())
        .when()
            .post("/users")
        .then()
            .statusCode(400)
            .body("errors", hasItem("Role must be STUDENT, INSTRUCTOR or ADMIN."));
    }

    private static class UserRequestBuilder {
        private String name = "John Doe";
        private String username = "johndoe";
        private String email = "john@doe.com";
        private String password = "password@123";
        private String role = "STUDENT";

        private String result = "";

        static UserRequestBuilder builder() {
            return new UserRequestBuilder();
        }

        public UserRequestBuilder blank() {
            name = "";
            username = "";
            email = "";
            password = "";
            role = "";
            return this;
        }

        public UserRequestBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserRequestBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserRequestBuilder withRole(String role) {
            this.role = role;
            return this;
        }

        public String name() {
            return name;
        }

        public String username() {
            return username;
        }

        public String email() {
            return email;
        }

        public String password() {
            return password;
        }

        public String role() {
            return role;
        }

        public String build() {
            return result;
        }

        public UserRequestBuilder post() {
            result =  """
            {
                "name": "%s",
                "username": "%s",
                "email": "%s",
                "password": "%s",
                "role": "%s"
            }
            """.formatted(name, username, email, password, role);

            return this;
        }
    }
}
