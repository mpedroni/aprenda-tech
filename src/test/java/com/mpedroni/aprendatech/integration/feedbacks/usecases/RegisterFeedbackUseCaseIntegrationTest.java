package com.mpedroni.aprendatech.integration.feedbacks.usecases;

import com.mpedroni.aprendatech.PersistenceTest;
import com.mpedroni.aprendatech.domain.feedbacks.exceptions.UserNotEnrolledException;
import com.mpedroni.aprendatech.domain.feedbacks.usecases.RegisterFeedbackCommand;
import com.mpedroni.aprendatech.domain.feedbacks.usecases.RegisterFeedbackUseCase;
import com.mpedroni.aprendatech.domain.providers.EmailSender;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaEntity;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaRepository;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@PersistenceTest
public class RegisterFeedbackUseCaseIntegrationTest {
    @Autowired
    FeedbackJpaRepository feedbackRepository;

    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    EnrollmentJpaRepository enrollmentRepository;

    EmailSender emailSender = mock();

    RegisterFeedbackUseCase sut;

    @BeforeEach
    void setup() {
        sut = new RegisterFeedbackUseCase(feedbackRepository, userRepository, courseRepository, enrollmentRepository, emailSender);
    }

    @Test
    void registerFeedback() {
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var aCourse = courseRepository.save(new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId()));

        enrollmentRepository.save(new EnrollmentJpaEntity(aCourse.getId(), aStudent.getId()));

        var aRating = 10f;
        var aReason = "reason";

        var aCommand = RegisterFeedbackCommand.with(
            aCourse.getId(),
            aStudent.getUsername(),
            aRating,
            aReason
        );

        enrollmentRepository.findAll().forEach(System.out::println);

        var feedback = sut.execute(aCommand);

        var persistedFeedback = feedbackRepository.findById(feedback.getId()).orElseThrow();

        assertThat(persistedFeedback.getCourseId(), is(aCourse.getId()));
        assertThat(persistedFeedback.getUserId(), is(aStudent.getId()));
        assertThat(persistedFeedback.getRating(), is(aRating));
        assertThat(persistedFeedback.getReason(), is(aReason));
    }

    @Test
    void throwsExceptionWhenUserDoesNotExist() {
        var aCommand = RegisterFeedbackCommand.with(
            1L,
            "nonExistentUser",
            10f,
            "reason"
        );

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> sut.execute(aCommand)
        );

        assertThat(exception.getMessage(), is("User not found."));
    }

    @Test
    void throwsExceptionWhenCourseDoesNotExist() {
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));

        var aCommand = RegisterFeedbackCommand.with(
            1L,
            aStudent.getUsername(),
            10f,
            "reason"
        );

        var exception = assertThrows(
                IllegalArgumentException.class,
                () -> sut.execute(aCommand)
        );

        assertThat(exception.getMessage(), is("Course not found."));
    }

    @Test
    void throwsExceptionWhenUserIsNotEnrolledInCourse() {
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var aCourse = courseRepository.save(new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId()));

        var aCommand = RegisterFeedbackCommand.with(
            aCourse.getId(),
            aStudent.getUsername(),
            10f,
            "reason"
        );

        assertThrows(
            UserNotEnrolledException.class,
            () -> sut.execute(aCommand)
        );
    }

    @Test
    void sendsEmailToInstructorWhenFeedbackRatingIsLessThanSix() {
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var aCourse = courseRepository.save(new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId()));

        aCourse.setInstructor(anInstructor);

        enrollmentRepository.save(new EnrollmentJpaEntity(aCourse.getId(), aStudent.getId()));

        var aRating = 5f;

        var aCommand = RegisterFeedbackCommand.with(
            aCourse.getId(),
            aStudent.getUsername(),
            aRating,
            "reason"
        );

        sut.execute(aCommand);

        aCourse.setInstructor(null);

        verify(emailSender).send(
            eq(anInstructor.getEmail()),
            any(),
            any()
        );
    }
}
