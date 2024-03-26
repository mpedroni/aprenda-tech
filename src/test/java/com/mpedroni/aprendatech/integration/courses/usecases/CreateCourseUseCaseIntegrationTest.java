package com.mpedroni.aprendatech.integration.courses.usecases;

import com.mpedroni.aprendatech.PersistenceTest;
import com.mpedroni.aprendatech.domain.courses.exceptions.CourseCodeAlreadyExistsException;
import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseCommand;
import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseUseCase;
import com.mpedroni.aprendatech.domain.users.exceptions.UserNotFoundException;
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

        sut = new CreateCourseUseCase(courseRepository, userRepository);
    }

    @Test
    void throwsAnErrorWhenCreatingACourseWithAnExistingCode() {
        var anInstructor = new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "password@123", "INSTRUCTOR");
        userRepository.save(anInstructor);

        var aCommand = CreateCourseCommand.with("Java Course", "java", anInstructor.getId(), "Java course");

        sut.execute(aCommand);

        assertThrows(
            CourseCodeAlreadyExistsException.class,
            () -> sut.execute(aCommand)
        );
    }

    @Test
    void throwsAnErrorWhenTheInstructorDoesNotExist() {
        var aCommand = CreateCourseCommand.with("Java Course", "java", 1L, "Java course");

        assertThrows(
            UserNotFoundException.class,
            () -> sut.execute(aCommand)
        );
    }

    @Test
    void throwsAnErrorWhenTheInstructorHasNotTheRoleInstructor() {
        var notAnInstructor = new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "password@123", "STUDENT");
        userRepository.save(notAnInstructor);

        var aCommand = CreateCourseCommand.with("Java Course", "java", notAnInstructor.getId(), "Java course");

        System.out.println(notAnInstructor);

        assertThrows(
                IllegalArgumentException.class,
                () -> sut.execute(aCommand)
        );
    }
}
