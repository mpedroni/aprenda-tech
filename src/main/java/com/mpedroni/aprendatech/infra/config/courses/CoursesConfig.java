package com.mpedroni.aprendatech.infra.config.courses;

import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseUseCase;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoursesConfig {
    private final CourseJpaRepository courseRepository;

    public CoursesConfig(CourseJpaRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Bean
    public CreateCourseUseCase createCourseUseCase() {
        return new CreateCourseUseCase(courseRepository);
    }
}
