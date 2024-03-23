package com.mpedroni.aprendatech.infra.users.api;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotBlank(message = "Name is required.")
        String name,

        @NotBlank(message = "Username is required.")
        @Size(max = 20, message = "Username must have at most 20 characters.")
        @Pattern(regexp = "^[a-z]+$", message = "Username must have only lower case characters, without numbers or blank spaces.")
        String username,

        @NotBlank(message = "Email is required.")
        @Email(regexp = ".+@.+\\..+", message = "Email must be a well-formed email address.")
        String email,

        @NotBlank(message = "Password is required.")
        String password,

        @NotBlank(message = "Role is required.")
        @Pattern(regexp = "^(STUDENT|INSTRUCTOR|ADMIN)$", message = "Role must be STUDENT, INSTRUCTOR or ADMIN.")
        String role
) {
}
