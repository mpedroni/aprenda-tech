package com.mpedroni.aprendatech.domain.courses.usecases;

import com.mpedroni.aprendatech.domain.courses.exceptions.CourseCodeAlreadyExistsException;
import com.mpedroni.aprendatech.domain.users.exceptions.UserNotFoundException;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;

public class CreateCourseUseCase {
    private final CourseJpaRepository courseRepository;
    private final UserJpaRepository userRepository;

    public CreateCourseUseCase(CourseJpaRepository courseRepository, UserJpaRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }


    public CourseJpaEntity execute(CreateCourseCommand command) {
        courseRepository.findByCode(command.code()).ifPresent(course -> {
            throw new CourseCodeAlreadyExistsException(command.code());
        });

        var user = userRepository.findById(command.instructorId()).orElseThrow(() -> new UserNotFoundException(command.instructorId()));

        if(!user.getRole().equals("INSTRUCTOR")) {
            throw new IllegalArgumentException("The given user is not an instructor.");
        }

        var course = new CourseJpaEntity(
                command.name(),
                command.code(),
                command.description(),
                user.getId()
        );

        return courseRepository.save(course);
    }
}
