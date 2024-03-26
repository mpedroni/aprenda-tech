package com.mpedroni.aprendatech.domain.enrollments.usecases;

import com.mpedroni.aprendatech.domain.courses.exceptions.CourseNotFoundException;
import com.mpedroni.aprendatech.domain.users.exceptions.UserNotFoundException;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaEntity;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;

public class EnrollUserUseCase {
    private final UserJpaRepository userRepository;
    private final CourseJpaRepository courseRepository;
    private final EnrollmentJpaRepository enrollmentRepository;

    public EnrollUserUseCase(UserJpaRepository userRepository, CourseJpaRepository courseRepository, EnrollmentJpaRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public EnrollmentJpaEntity execute(EnrollUserCommand command) {
        var user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new UserNotFoundException(command.username()));

        var course = courseRepository.findById(command.courseId())
                .orElseThrow(() -> new CourseNotFoundException(command.courseId()));

        var enrollment = new EnrollmentJpaEntity(course.getId(), user.getId());

        return enrollmentRepository.save(enrollment);
    }
}
