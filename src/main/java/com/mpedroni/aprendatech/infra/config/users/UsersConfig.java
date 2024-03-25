package com.mpedroni.aprendatech.infra.config.users;

import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsersConfig {
    @Bean
    public CreateUserUseCase createUserUseCase(
            UserJpaRepository repository,
            PasswordEncoder encoder
    ) {
        return new CreateUserUseCase(repository, encoder);
    }
}
