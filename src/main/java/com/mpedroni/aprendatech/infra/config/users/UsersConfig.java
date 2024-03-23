package com.mpedroni.aprendatech.infra.config.users;

import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsersConfig {
    @Bean
    @Autowired
    public CreateUserUseCase createUserUseCase(
            UserJpaRepository repository
    ) {
        return new CreateUserUseCase(repository);
    }
}
