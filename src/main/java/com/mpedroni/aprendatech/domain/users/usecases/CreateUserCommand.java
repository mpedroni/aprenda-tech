package com.mpedroni.aprendatech.domain.users.usecases;

public record CreateUserCommand(
        String name,
        String username,
        String email,
        String password,
        String role) {

    public static CreateUserCommand with(String name, String username, String email, String password, String role) {
        return new CreateUserCommand(name, username, email, password, role);
    }
}
