package com.mpedroni.aprendatech.integration.courses.usecases;

import com.mpedroni.aprendatech.PersistenceTest;
import com.mpedroni.aprendatech.domain.courses.exceptions.CourseCodeAlreadyExistsException;
import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseCommand;
import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseUseCase;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertThrows;

@PersistenceTest
public class CreateCourseUseCaseIntegrationTest {
    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    UserJpaRepository userRepository;

    CreateCourseUseCase sut;

    @BeforeEach
    void setup() {
        courseRepository.deleteAll();
        userRepository.deleteAll();

        sut = new CreateCourseUseCase(courseRepository);
    }

    @Test
    void throwsAnErrorWhenCreatingACourseWithAnExistingCode() {
        var aCommand = CreateCourseCommand.with("Java Course", "java", 1L, "Java course");
        var anInstructor = new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "password@123", "INSTRUCTOR");

        userRepository.save(anInstructor);

        sut.execute(aCommand);

        assertThrows(
            CourseCodeAlreadyExistsException.class,
            () -> sut.execute(aCommand)
        );
    }
}
