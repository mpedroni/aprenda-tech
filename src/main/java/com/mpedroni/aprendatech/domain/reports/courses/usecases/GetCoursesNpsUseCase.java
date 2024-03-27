package com.mpedroni.aprendatech.domain.reports.courses.usecases;

import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class GetCoursesNpsUseCase {
    private final CourseJpaRepository courseRepository;

    public GetCoursesNpsUseCase(CourseJpaRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Page<CourseNps> execute(GetCoursesNpsCommand command) {
        PageRequest pageRequest = PageRequest.of(command.page(), command.perPage());

        return courseRepository.findNpsByCourses(pageRequest).map(f -> {
            var total = f.promoters() + f.detractors() + f.passives();
            var nps = calculateNps(f.promoters(), f.detractors(), total);

            return new CourseNps(f.courseId(), f.code(), f.promoters(), f.detractors(), f.passives(), total, nps);
        });
    }

    private int calculateNps(int promoters, int detractors, int total) {
        return (int) Math.round((promoters - detractors) / (double) total * 100);
    }
}
