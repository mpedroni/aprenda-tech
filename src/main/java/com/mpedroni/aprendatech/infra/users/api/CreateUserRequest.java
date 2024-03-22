package com.mpedroni.aprendatech.infra.users.api;

public record CreateUserRequest(
        String name,
        String username,
        String email,
        String password,
        String role
) {
}
