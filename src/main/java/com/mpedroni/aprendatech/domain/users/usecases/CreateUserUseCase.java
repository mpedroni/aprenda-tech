package com.mpedroni.aprendatech.domain.users.usecases;

import com.mpedroni.aprendatech.domain.users.exceptions.EmailAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.exceptions.UsernameAlreadyExistsException;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;

public class CreateUserUseCase {
    private final UserJpaRepository repository;

    public CreateUserUseCase(UserJpaRepository repository) {
        this.repository = repository;
    }

    public UserJpaEntity execute(CreateUserCommand command) {
        var name = command.name();
        var username = command.username();
        var email = command.email();
        var password = command.password();
        var role = command.role();

        repository.findByUsername(username).ifPresent(user -> {
            throw new UsernameAlreadyExistsException(username);
        });

        repository.findByEmail(email).ifPresent(user -> {
            throw new EmailAlreadyExistsException(email);
        });

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
