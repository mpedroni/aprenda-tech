package com.mpedroni.aprendatech.domain.feedbacks.usecases;

import com.mpedroni.aprendatech.domain.feedbacks.exceptions.UserNotEnrolledException;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaRepository;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaEntity;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;

public class RegisterFeedbackUseCase {
    private final FeedbackJpaRepository feedbackRepository;
    private final UserJpaRepository userRepository;
    private final CourseJpaRepository courseRepository;
    private final EnrollmentJpaRepository enrollmentRepository;

    public RegisterFeedbackUseCase(FeedbackJpaRepository feedbackRepository, UserJpaRepository userRepository, CourseJpaRepository courseRepository, EnrollmentJpaRepository enrollmentRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public FeedbackJpaEntity execute(RegisterFeedbackCommand command) {
        var user = userRepository.findByUsername(command.username())
            .orElseThrow(() -> new IllegalArgumentException("User not found."));

        var course = courseRepository.findById(command.courseId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found."));

        var feedback = new FeedbackJpaEntity(
            course.getId(),
            user.getId(),
            command.rating(),
            command.reason().orElse(null)
        );

        enrollmentRepository.findByUserIdAndCourseId(user.getId(), course.getId())
            .orElseThrow(() -> new UserNotEnrolledException(course.getCode()));

        return feedbackRepository.save(feedback);
    }
}
