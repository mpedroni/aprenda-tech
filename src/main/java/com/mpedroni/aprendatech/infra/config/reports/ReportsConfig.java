package com.mpedroni.aprendatech.infra.config.reports;

import com.mpedroni.aprendatech.domain.reports.courses.usecases.GetCoursesNpsUseCase;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportsConfig {
    private final CourseJpaRepository courseRepository;

    public ReportsConfig(CourseJpaRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Bean
    public GetCoursesNpsUseCase getCoursesNpsUseCase() {
        return new GetCoursesNpsUseCase(courseRepository);
    }
}
