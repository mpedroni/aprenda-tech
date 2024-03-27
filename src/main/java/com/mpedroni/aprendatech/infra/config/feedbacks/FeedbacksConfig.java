package com.mpedroni.aprendatech.infra.config.feedbacks;

import com.mpedroni.aprendatech.domain.feedbacks.usecases.RegisterFeedbackUseCase;
import com.mpedroni.aprendatech.domain.providers.EmailSender;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaRepository;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedbacksConfig {
    private final FeedbackJpaRepository feedbackRepository;
    private final UserJpaRepository userRepository;
    private final CourseJpaRepository courseRepository;
    private final EnrollmentJpaRepository enrollmentRepository;
    private final EmailSender emailSender;

    public FeedbacksConfig(FeedbackJpaRepository feedbackRepository, UserJpaRepository userRepository, CourseJpaRepository courseRepository, EnrollmentJpaRepository enrollmentRepository, EmailSender emailSender) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.emailSender = emailSender;
    }

    @Bean
    public RegisterFeedbackUseCase registerFeedbackUseCase() {
        return new RegisterFeedbackUseCase(feedbackRepository, userRepository, courseRepository, enrollmentRepository, emailSender);
    }
}
