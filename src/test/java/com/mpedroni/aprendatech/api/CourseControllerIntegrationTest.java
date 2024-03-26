package com.mpedroni.aprendatech.api;

import com.mpedroni.aprendatech.ControllerTest;
import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseUseCase;
import com.mpedroni.aprendatech.infra.courses.api.CourseController;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@ControllerTest(controllers = CourseController.class)
public class CourseControllerIntegrationTest {
    @MockBean
    CreateCourseUseCase createCourseUseCase;

    @Test
    void throwsWhenCodeHasInvalidLength() {
        var aCodeWithMoreThan10Characters = "a".repeat(11);
        var body = new CreateCourseRequestBody().withCode(aCodeWithMoreThan10Characters).build();

        givenCreateCourseRequest(body)
        .then()
            .statusCode(400)
            .body("errors", hasItems("Code must have at most 10 characters."));
    }

    @Test
    void throwsWhenCodeHasInvalidCharacters() {
        var aCodeWithInvalidCharacters = "j4va@";
        var body = new CreateCourseRequestBody().withCode(aCodeWithInvalidCharacters).build();

        givenCreateCourseRequest(body)
        .then()
            .statusCode(400)
            .body("errors", hasItems("Code must contain only letters and hyphens."));
    }

    @Test
    void throwsAnErrorWhenRequiredFieldsAreBlank() {
        var body = new CreateCourseRequestBody().blank().build();

        givenCreateCourseRequest(body)
        .then()
            .statusCode(400)
            .body("errors", hasItems(
                    "Name is required.",
                    "Code is required.",
                    "Instructor ID is required.",
                    "Description is required."
            ));
    }

    private class CreateCourseRequestBody {
        private String name = "Java Course";
        private String code = "java";
        private Long instructorId = 1L;
        private String description = "Become a Java expert";

        CreateCourseRequestBody withCode(String code) {
            this.code = code;
            return this;
        }

        public String name() {
            return name;
        }

        public String code() {
            return code;
        }

        public Long instructorId() {
            return instructorId;
        }

        public String description() {
            return description;
        }

        public String build() {
            return """
                {
                  "name": "%s",
                  "code": "%s",
                  "instructorId": %d,
                  "description": "%s"
                }
            """.formatted(name, code, instructorId, description);
        }

        public CreateCourseRequestBody blank() {
            name = "";
            code = "";
            instructorId = null;
            description = "";
            return this;
        }
    }

    private MockMvcResponse givenCreateCourseRequest(String body) {
        return
            given()
                .auth()
                    .with(user("admin").roles("ADMIN"))
                .contentType("application/json")
                .body(body)
            .when()
                .post("/courses");
    }
}
