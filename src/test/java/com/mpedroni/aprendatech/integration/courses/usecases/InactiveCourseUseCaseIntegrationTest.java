package com.mpedroni.aprendatech.integration.courses.usecases;

import com.mpedroni.aprendatech.PersistenceTest;
import com.mpedroni.aprendatech.domain.courses.exceptions.CourseNotFoundException;
import com.mpedroni.aprendatech.domain.courses.usecases.InactivateCourseUseCase;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@PersistenceTest
public class InactiveCourseUseCaseIntegrationTest {
    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    UserJpaRepository userRepository;

    InactivateCourseUseCase sut;

    @BeforeEach
    void setup() {
        courseRepository.deleteAll();
        userRepository.deleteAll();

        sut = new InactivateCourseUseCase(courseRepository);
    }

    @Test
    void inactivatesACourse() {
        var instructor = userRepository.save(new UserJpaEntity(
                "John Doe",
                "johndoe",
                "john@doe.com",
                "pass",
                "INSTRUCTOR"
        ));
        var course = courseRepository.save(new CourseJpaEntity("Java", "JAVA", "Java course", instructor.getId()));

        sut.execute(course.getCode());

        var inactivatedCourse = courseRepository.findByCode(course.getCode()).orElseThrow();
        assertThat(inactivatedCourse.getStatus(), is(equalTo("INACTIVE")));
    }

    @Test
    void throwsAnErrorWhenTheCourseDoesNotExist() {
        assertThrows(CourseNotFoundException.class, () -> sut.execute("NON_EXISTENT"));
    }

}
