package com.mpedroni.aprendatech.api;

import com.mpedroni.aprendatech.ControllerTest;
import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.infra.users.api.UserController;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ControllerTest(controllers = UserController.class)
public class UserControllerUnitTest {
    @MockBean
    CreateUserUseCase createUserUseCase;

    @Test
    void createsAStudent() {
        var body = new CreateUserRequestBody();

        var name = body.name();
        var username = body.username();
        var email = body.email();
        var password = body.password();
        var role = body.role();

        when(createUserUseCase.execute(name, username, email, password, role))
            .thenReturn(
                UserJpaEntity.from(1L, body.name(), body.username(), body.email(), body.password(), body.role()
            )
        );

        givenCreateUserRequest(body.build())
        .then()
            .statusCode(201)
            .header("Location", matchesPattern("/users/[\\d+]"));
    }

    @Test
    void throwsAnErrorWhenRequiredFieldsAreBlank() {
        var body = new CreateUserRequestBody().blank().build();

        givenCreateUserRequest(body)
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
        var body = new CreateUserRequestBody().withUsername(aUsernameWithMoreThan20Characters).build();

        givenCreateUserRequest(body)
        .then()
            .statusCode(400)
            .body("errors", hasItem("Username must have at most 20 characters."));
    }

    @Test
    void throwsAnErrorWhenUsernameHasInvalidCharacters() {
        var aUsernameWithInvalidCharacters = "J0hn Do3@";
        var body = new CreateUserRequestBody().withUsername(aUsernameWithInvalidCharacters).build();

        givenCreateUserRequest(body)
        .then()
            .statusCode(400)
            .body("errors", hasItem("Username must have only lower case characters, without numbers or blank spaces."));
    }

    @Test
    void throwsAnErrorWhenEmailIsInvalid() {
        var anInvalidEmail = "john@doe";
        var body = new CreateUserRequestBody().withEmail(anInvalidEmail).build();

        givenCreateUserRequest(body)
        .then()
            .statusCode(400)
            .body("errors", hasItem("Email must be a well-formed email address."));
    }

    @Test
    void throwsAnErrorWhenRoleIsInvalid() {
        var anInvalidRole = "INVALID";
        var body = new CreateUserRequestBody().withRole(anInvalidRole).build();

        givenCreateUserRequest(body)
        .then()
            .statusCode(400)
            .body("errors", hasItem("Role must be STUDENT, INSTRUCTOR or ADMIN."));
    }

    static MockMvcResponse givenCreateUserRequest(String body) {
        return given()
            .auth().none()
            .contentType("application/json")
            .body(body)
        .when()
            .post("/users");
    }

    private static class CreateUserRequestBody {
        private String name = "John Doe";
        private String username = "johndoe";
        private String email = "john@doe.com";
        private String password = "password@123";
        private String role = "STUDENT";

        public CreateUserRequestBody() {}

        public CreateUserRequestBody blank() {
            name = "";
            username = "";
            email = "";
            password = "";
            role = "";
            return this;
        }

        public CreateUserRequestBody withUsername(String username) {
            this.username = username;
            return this;
        }

        public CreateUserRequestBody withEmail(String email) {
            this.email = email;
            return this;
        }

        public CreateUserRequestBody withRole(String role) {
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
            return """
            {
                "name": "%s",
                "username": "%s",
                "email": "%s",
                "password": "%s",
                "role": "%s"
            }
            """.formatted(name, username, email, password, role);
        }
    }
}
