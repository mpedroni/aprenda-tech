package com.mpedroni.aprendatech.e2e;

import com.mpedroni.aprendatech.E2ETest;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.providers.JwtProvider;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

@E2ETest
public class CourseE2ETest {
    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    JwtProvider jwt;

    @BeforeEach
    void setup() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createsACourseWhenTheAuthenticatedUserIsAnAdmin() {
        var anInstructor = new UserJpaEntity("John Doe", "johndoe", "john@doe.com", "pass", "INSTRUCTOR");
        var anAdmin = new UserJpaEntity(
                "Administrator",
                "admin",
                "admin@email.com",
                "password@123",
                "ADMIN"
        );

        userRepository.saveAll(List.of(anInstructor, anAdmin));

        var aName = "Java Course";
        var aCode = "java";
        var anInstructorId = anInstructor.getId();
        var aDescription = "Become a Java expert";

        var token = jwt.generate(anAdmin.getUsername(), anAdmin.getRole());

        given()
            .header("Authorization", "Bearer %s".formatted(token))
            .body("""
                {
                  "name": "%s",
                  "code": "%s",
                  "instructorId": %d,
                  "description": "%s"
                }
             """.formatted(aName, aCode, anInstructorId, aDescription)
            )
            .contentType("application/json")
        .when()
            .post("/courses")
        .then()
            .statusCode(201);
    }
}
