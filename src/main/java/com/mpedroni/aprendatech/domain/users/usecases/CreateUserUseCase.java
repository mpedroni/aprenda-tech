package com.mpedroni.aprendatech.domain.users.usecases;

import com.mpedroni.aprendatech.domain.users.exceptions.EmailAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.exceptions.UsernameAlreadyExistsException;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUserUseCase {
    private final UserJpaRepository repository;
    private final PasswordEncoder encoder;

    public CreateUserUseCase(UserJpaRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
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

        var encryptedPassword = encoder.encode(password);

        var user = new UserJpaEntity(
                name,
                username,
                email,
                encryptedPassword,
                role
        );

        repository.save(user);

        return user;
    }
}
