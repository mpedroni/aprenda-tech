package com.mpedroni.aprendatech.integration.users.usecases;

import com.mpedroni.aprendatech.domain.users.exceptions.EmailAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.exceptions.UsernameAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.usecases.CreateUserCommand;
import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test-e2e")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreateUserUseCaseIntegrationTest {
    @Autowired
    UserJpaRepository userRepository;

    CreateUserUseCase sut;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        sut = new CreateUserUseCase(userRepository);
    }

    @Test
    void throwsAnErrorWhenCreatingAUserWithAnExistingUsername() {
        var name = "John Doe";
        var username = "johndoe";
        var email = "john@doe.com";
        var password = "password@123";
        var role = "STUDENT";
        var aCommand = CreateUserCommand.with(name, username, email, password, role);
        var anotherCommand = CreateUserCommand.with(name, username, "anotherjohn@doe.com", password, role);

        sut.execute(aCommand);

        assertThrows(
            UsernameAlreadyExistsException.class,
            () -> sut.execute(anotherCommand)
        );
    }

    @Test
    void throwsAnErrorWhenCreatingAUserWithAnExistingEmail() {
        var name = "John Doe";
        var username = "johndoe";
        var email = "john@doe.com";
        var password = "password@123";
        var role = "STUDENT";
        var aCommand = CreateUserCommand.with(name, username, email, password, role);
        var anotherCommand = CreateUserCommand.with(name, "anotherjohndoe", email, password, role);

        sut.execute(aCommand);

        assertThrows(
            EmailAlreadyExistsException.class,
            () -> sut.execute(anotherCommand)
        );
    }
}
