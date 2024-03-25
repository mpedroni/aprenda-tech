package com.mpedroni.aprendatech.domain.users.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("A user with the username \"" + username + "\" already exists.");
    }
}
