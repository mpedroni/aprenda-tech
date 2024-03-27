package com.mpedroni.aprendatech.domain.feedbacks.usecases;

import com.mpedroni.aprendatech.domain.enrollments.exceptions.CourseInactiveException;
import com.mpedroni.aprendatech.domain.feedbacks.exceptions.UserNotEnrolledException;
import com.mpedroni.aprendatech.domain.providers.EmailSender;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
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
    private final EmailSender emailSender;

    public RegisterFeedbackUseCase(FeedbackJpaRepository feedbackRepository, UserJpaRepository userRepository, CourseJpaRepository courseRepository, EnrollmentJpaRepository enrollmentRepository, EmailSender emailSender) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.emailSender = emailSender;
    }

    public FeedbackJpaEntity execute(RegisterFeedbackCommand command) {
        var user = userRepository.findByUsername(command.username())
            .orElseThrow(() -> new IllegalArgumentException("User not found."));

        var course = courseRepository.findById(command.courseId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found."));

        if (!course.isActive()) {
            throw new CourseInactiveException(course.getCode());
        }

        var feedback = new FeedbackJpaEntity(
            course.getId(),
            user.getId(),
            command.rating(),
            command.reason().orElse(null)
        );

        enrollmentRepository.findByUserIdAndCourseId(user.getId(), course.getId())
            .orElseThrow(() -> new UserNotEnrolledException(course.getCode()));

        feedbackRepository.save(feedback);

        var isDetraction = Float.compare(feedback.getRating(), 6f) == -1;

        if (isDetraction) {
            notifyCourseInstructor(feedback, course);
        }

        return feedback;
    }

    private void notifyCourseInstructor(FeedbackJpaEntity feedback, CourseJpaEntity course) {
        try {
            emailSender.send(
                    course.getInstructor().getEmail(),
                    "Feedback received for your course %s".formatted(course.getName()),
                    """
                    A student has given a rating of %.1f to your course Java for Beginners. Please check the feedback:
                        
                        %s
                    """
                            .formatted(feedback.getRating(), feedback.getReason())
            );
        } catch (Exception ex) {
            System.err.printf("[ERROR] Error while sending email to instructor %s%n", course.getInstructor().getEmail());
        }
    }
}
