package com.mpedroni.aprendatech.domain.users.usecases;

import com.mpedroni.aprendatech.domain.users.exceptions.UserNotFoundException;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;

public class FindUserByUsernameUseCase {
    private final UserJpaRepository repository;

    public FindUserByUsernameUseCase(UserJpaRepository repository) {
        this.repository = repository;
    }

    public UserJpaEntity execute(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
