package com.mpedroni.aprendatech.infra.config.users;

import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.domain.users.usecases.FindUserByUsernameUseCase;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsersConfig {
    private final UserJpaRepository repository;

    public UsersConfig(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Bean
    public CreateUserUseCase createUserUseCase(PasswordEncoder encoder) {
        return new CreateUserUseCase(repository, encoder);
    }

    @Bean
    public FindUserByUsernameUseCase findUserByUsernameUseCase() {
        return new FindUserByUsernameUseCase(repository);
    }
}
