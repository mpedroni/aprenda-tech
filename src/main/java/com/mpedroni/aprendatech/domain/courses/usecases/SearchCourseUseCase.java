package com.mpedroni.aprendatech.domain.courses.usecases;

import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class SearchCourseUseCase {
    private final CourseJpaRepository courseRepository;

    public SearchCourseUseCase(CourseJpaRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Page<CourseJpaEntity> execute(SearchCourseCommand command) {
        var request = PageRequest.of(command.page(), command.perPage());

        if (command.status().isEmpty()) {
            return courseRepository.findAll(request);
        }

        return courseRepository.findByStatus(command.status().get(), request);
    }
}
