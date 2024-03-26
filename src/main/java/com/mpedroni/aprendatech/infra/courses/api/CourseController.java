package com.mpedroni.aprendatech.infra.courses.api;

import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseCommand;
import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CreateCourseUseCase createCourseUseCase;

    public CourseController(CreateCourseUseCase createCourseUseCase) {
        this.createCourseUseCase = createCourseUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateCourseRequest request) {
        var aCommand = CreateCourseCommand.with(
                request.name(),
                request.code(),
                request.instructorId(),
                request.description()
        );
        var course = createCourseUseCase.execute(aCommand);
        var location = URI.create("/courses/" + course.getId());

        return ResponseEntity.created(location).build();
    }
}
