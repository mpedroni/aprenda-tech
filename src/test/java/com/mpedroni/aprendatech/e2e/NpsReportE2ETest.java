package com.mpedroni.aprendatech.e2e;

import com.mpedroni.aprendatech.E2ETest;
import com.mpedroni.aprendatech.domain.reports.utils.NpsCalculator;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaEntity;
import com.mpedroni.aprendatech.infra.courses.persistence.CourseJpaRepository;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaEntity;
import com.mpedroni.aprendatech.infra.enrollments.persistence.EnrollmentJpaRepository;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaEntity;
import com.mpedroni.aprendatech.infra.feedbacks.persistence.FeedbackJpaRepository;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaEntity;
import com.mpedroni.aprendatech.infra.users.persistence.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@E2ETest
public class NpsReportE2ETest {
    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    EnrollmentJpaRepository enrollmentRepository;

    @Autowired
    FeedbackJpaRepository feedbackRepository;

    @Test
    void showsReportWithNpsMetricByCourse() {
        var instructor = userRepository.save(
            new UserJpaEntity(
                    "John Doe",
                    "johndoe",
                    "john@doe.com",
                    "pass",
                    "INSTRUCTOR"
            )
        );

        var students = List.of(
            aStudent(1),
            aStudent(2),
            aStudent(3),
            aStudent(4),
            aStudent(5)
        );
        userRepository.saveAll(students);

        var java = courseRepository.save(
            new CourseJpaEntity(
                    "Java Course",
                    "java",
                    "Learn Java",
                    instructor.getId()
            )
        );

        var enrollments = students.stream().map(s ->
            new EnrollmentJpaEntity(java.getId(), s.getId())
        ).toList();

        enrollmentRepository.saveAll(enrollments);

        var student = students.getFirst();

        var feedbacks = List.of(
            // promoters
            new FeedbackJpaEntity(java.getId(), student.getId(), 10f, ""),
            new FeedbackJpaEntity(java.getId(), student.getId(), 9f, ""),

            // detractors
            new FeedbackJpaEntity(java.getId(), student.getId(), 4f, ""),

            // passives
            new FeedbackJpaEntity(java.getId(), student.getId(), 7f, "")
        );

        feedbackRepository.saveAll(feedbacks);

        var promoters = (int) feedbacks.stream().filter(f -> f.getRating() >= 9).count();
        var detractors = (int) feedbacks.stream().filter(f -> f.getRating() <= 6).count();
        var passives = feedbacks.size() - promoters - detractors;
        var nps = NpsCalculator.nps(promoters, detractors, feedbacks.size());

        given()
            .auth()
            .with(user("admin").roles("ADMIN"))
        .when()
            .get("/reports/courses/nps")
        .then()
            .body("content[0].id", is(java.getId().intValue()))
            .body("content[0].code", is(java.getCode()))
            .body("content[0].promoters", is(promoters))
            .body("content[0].detractors", is(detractors))
            .body("content[0].passives", is(passives))
            .body("content[0].total", is(feedbacks.size()))
            .body("content[0].nps", is(nps))
            .statusCode(200);
    }

    private UserJpaEntity aStudent(Integer number) {
        return new UserJpaEntity(
                number.toString(),
                number.toString(),
                "%d@email.com".formatted(number),
                number.toString(),
                "STUDENT"
        );
    }
}
