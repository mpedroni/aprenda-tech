package com.mpedroni.aprendatech.integration.users.usecases;

import com.mpedroni.aprendatech.PersistenceTest;
import com.mpedroni.aprendatech.domain.users.exceptions.EmailAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.exceptions.UsernameAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.usecases.CreateUserCommand;
import com.mpedroni.aprendatech.domain.users.usecases.CreateUserUseCase;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@PersistenceTest
public class CreateUserUseCaseIntegrationTest {
    @Autowired
    UserJpaRepository userRepository;
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    CreateUserUseCase sut;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        sut = new CreateUserUseCase(userRepository, encoder);
    }

    @Test
    void createsAUserWithAnEncryptedPassword() {
        var name = "John Doe";
        var username = "johndoe";
        var email = "john@doe.com";
        var password = "password@123";
        var role = "STUDENT";
        var aCommand = CreateUserCommand.with(name, username, email, password, role);

        sut.execute(aCommand);

        var user = userRepository.findByUsername(username).orElseThrow();

        assertThat(user.getName(), is(equalTo(name)));
        assertThat(user.getUsername(), is(equalTo(username)));
        assertThat(user.getEmail(), is(equalTo(email)));
        assertTrue(encoder.matches(password, user.getPassword()));
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
