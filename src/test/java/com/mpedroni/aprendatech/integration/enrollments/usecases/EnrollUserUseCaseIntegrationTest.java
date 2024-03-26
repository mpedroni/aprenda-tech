package com.mpedroni.aprendatech.integration.enrollments.usecases;

import com.mpedroni.aprendatech.PersistenceTest;
import com.mpedroni.aprendatech.domain.courses.exceptions.CourseNotFoundException;
import com.mpedroni.aprendatech.domain.enrollments.exceptions.CourseInactiveException;
import com.mpedroni.aprendatech.domain.enrollments.exceptions.UserAlreadyEnrolledException;
import com.mpedroni.aprendatech.domain.enrollments.usecases.EnrollUserCommand;
import com.mpedroni.aprendatech.domain.enrollments.usecases.EnrollUserUseCase;
import com.mpedroni.aprendatech.domain.users.exceptions.UserNotFoundException;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@PersistenceTest
public class EnrollUserUseCaseIntegrationTest {
    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    EnrollmentJpaRepository enrollmentRepository;

    EnrollUserUseCase sut;

    @BeforeEach
    void setUp() {
        sut = new EnrollUserUseCase(userRepository, courseRepository, enrollmentRepository);
    }

    @Test
    void enrollsUserInTheGivenCourse() {
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));
        var aCourse = courseRepository.save(new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId()));
        var aCommand = EnrollUserCommand.with(aCourse.getId(), aStudent.getUsername());

        var enrollment = sut.execute(aCommand);

        assertThat(enrollment.getId(), notNullValue());
        assertThat(enrollment.getCourseId(), is(aCourse.getId()));
        assertThat(enrollment.getUserId(), is(aStudent.getId()));
    }

    @Test
    void throwsExceptionWhenUserIsNotFound() {
        var aCommand = EnrollUserCommand.with(1L, "unknown");

        assertThrows(UserNotFoundException.class, () -> sut.execute(aCommand));
    }

    @Test
    void throwsExceptionWhenCourseIsNotFound() {
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var unknownCourseId = 1L;
        var aCommand = EnrollUserCommand.with(unknownCourseId, aStudent.getUsername());

        assertThrows(CourseNotFoundException.class, () -> sut.execute(aCommand));
    }

    @Test
    void throwsExceptionWhenUserIsAlreadyEnrolled() {
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));
        var aCourse = courseRepository.save(new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId()));
        var aCommand = EnrollUserCommand.with(aCourse.getId(), aStudent.getUsername());

        sut.execute(aCommand);

        assertThrows(UserAlreadyEnrolledException.class, () -> sut.execute(aCommand));
    }

    @Test
    void throwsExceptionWhenCourseIsInactive() {
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));

        var aCourse = new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId());
        aCourse.inactivate();
        courseRepository.save(aCourse);

        var aCommand = EnrollUserCommand.with(aCourse.getId(), aStudent.getUsername());

        assertThrows(CourseInactiveException.class, () -> sut.execute(aCommand));
    }
}
