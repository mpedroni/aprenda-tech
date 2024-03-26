package com.mpedroni.aprendatech.integration.courses.usecases;

import com.mpedroni.aprendatech.PersistenceTest;
import com.mpedroni.aprendatech.domain.courses.usecases.SearchCourseUseCase;
import com.mpedroni.aprendatech.domain.courses.usecases.SearchCourseCommand;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@PersistenceTest
public class SearchCouseUseCaseIntegrationTest {
    static UserJpaEntity anInstructor;
    static CourseJpaEntity javaCourse;
    static CourseJpaEntity dotnetCourse;
    static CourseJpaEntity pythonCourse;

    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    UserJpaRepository userRepository;

    SearchCourseUseCase sut;

    @BeforeAll
    static void beforeAll(@Autowired UserJpaRepository userRepository, @Autowired CourseJpaRepository courseRepository) {
        anInstructor = userRepository.save(new UserJpaEntity(
            "John Doe",
            "johndoe",
            "john@doe.com",
            "pass",
            "INSTRUCTOR"
        ));

        javaCourse = new CourseJpaEntity(
            "Java Course",
            "java",
            "Learn Java",
            anInstructor.getId()
        );
        dotnetCourse = new CourseJpaEntity(
            ".NET Course",
            "dotnet",
            "Learn .NET",
            anInstructor.getId()
        );
        pythonCourse = new CourseJpaEntity(
            "Python Course",
            "python",
            "Learn Python",
            anInstructor.getId()
        );
        pythonCourse.inactivate();

        courseRepository.saveAll(List.of(javaCourse, dotnetCourse, pythonCourse));
    }

    @BeforeEach
    void setup() {
        sut = new SearchCourseUseCase(courseRepository);
    }

    @AfterAll
    static void afterAll(@Autowired UserJpaRepository userRepository, @Autowired CourseJpaRepository courseRepository) {
        courseRepository.deleteAll();
        userRepository.delete(anInstructor);
    }

    @Test
    void returnsPageOfCourses() {
        var aCommand = SearchCourseCommand.with(0, 2);
        var result = sut.execute(aCommand);

        assertThat(result.getContent(), hasSize(2));
        assertThat(result.getTotalPages(), is(2));
        assertThat(result.getTotalElements(), is(3L));
    }

    @Test
    void returnsPageOfActiveCoursesWhenFilterApplied() {
        var aCommand = SearchCourseCommand.with(0, 10, "ACTIVE");
        var result = sut.execute(aCommand);

        assertThat(result.getContent(), hasSize(2));
        assertThat(result.getTotalPages(), is(1));
        assertThat(result.getTotalElements(), is(2L));
    }
}
