package com.mpedroni.aprendatech.e2e;

import com.mpedroni.aprendatech.E2ETest;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.providers.JwtProvider;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@E2ETest
public class EnrollmentE2ETest {
    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    JwtProvider jwt;

    @Test
    void enrollStudentInTheActiveCourse() {
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var aCourse = courseRepository.save(new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId()));

        var token = jwt.generate(aStudent.getUsername(), aStudent.getRole());

        given()
            .header("Authorization", "Bearer %s".formatted(token))
            .body("{ \"courseId\": %d}".formatted(aCourse.getId()))
            .contentType("application/json")
        .when()
            .post("/enrollments")
        .then()
            .statusCode(201);
    }
}
