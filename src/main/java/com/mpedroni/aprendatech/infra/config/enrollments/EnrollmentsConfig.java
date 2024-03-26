package com.mpedroni.aprendatech.infra.config.enrollments;

import com.mpedroni.aprendatech.domain.enrollments.usecases.EnrollUserUseCase;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnrollmentsConfig {
    private final EnrollmentJpaRepository enrollmentRepository;
    private final UserJpaRepository userRepository;
    private final CourseJpaRepository courseRepository;

    public EnrollmentsConfig(EnrollmentJpaRepository enrollmentRepository, UserJpaRepository userRepository, CourseJpaRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Bean
    public EnrollUserUseCase enrollUserUseCase() {
        return new EnrollUserUseCase(
                userRepository,
                courseRepository,
                enrollmentRepository
        );
    }
}
