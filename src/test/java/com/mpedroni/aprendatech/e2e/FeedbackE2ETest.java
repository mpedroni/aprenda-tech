package com.mpedroni.aprendatech.e2e;

import com.mpedroni.aprendatech.E2ETest;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaRepository;
import com.mpedroni.aprendatech.infra.providers.JwtProvider;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@E2ETest
public class FeedbackE2ETest {
    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    JwtProvider jwt;

    @Test
    void registersAFeedback() {
        var anInstructor = userRepository.save(new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR"));
        var aStudent = userRepository.save(new UserJpaEntity("Bruce Wayne", "batman", "bruce@wayne.bat", "pass", "STUDENT"));
        var aCourse = courseRepository.save(new CourseJpaEntity("Java for Beginners", "java", "Learn Java from scratch", anInstructor.getId()));

        var token = jwt.generate(aStudent.getUsername(), aStudent.getRole());

        given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .body("""
                {
                    "courseId": %d,
                    "rating": 10,
                    "reason": "Great course!"
                }
                """.formatted(aCourse.getId()))
        .when()
            .post("/feedbacks")
        .then()
            .statusCode(201);
    }
}
