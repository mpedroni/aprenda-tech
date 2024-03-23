package com.mpedroni.aprendatech.domain.users.usecases;

import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;

public class CreateUserUseCase {
    private final UserJpaRepository repository;

    public CreateUserUseCase(UserJpaRepository repository) {
        this.repository = repository;
    }

    public UserJpaEntity execute(String name, String username, String email, String password, String role) {
        var user = new UserJpaEntity(
                name,
                username,
                email,
                password,
                role
        );

        repository.save(user);

        return user;
    }
}
