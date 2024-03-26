package com.mpedroni.aprendatech.infra.config.courses;

import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseUseCase;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoursesConfig {
    private final CourseJpaRepository courseRepository;
    private final UserJpaRepository userRepository;

    public CoursesConfig(CourseJpaRepository courseRepository, UserJpaRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Bean
    public CreateCourseUseCase createCourseUseCase() {
        return new CreateCourseUseCase(courseRepository, userRepository);
    }
}
