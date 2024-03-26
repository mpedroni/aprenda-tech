package com.mpedroni.aprendatech.infra.courses.api;

import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseCommand;
import com.mpedroni.aprendatech.domain.courses.usecases.CreateCourseUseCase;
import com.mpedroni.aprendatech.domain.courses.usecases.InactivateCourseUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CreateCourseUseCase createCourseUseCase;
    private final InactivateCourseUseCase inactivateCourseUseCase;

    public CourseController(CreateCourseUseCase createCourseUseCase, InactivateCourseUseCase inactivateCourseUseCase) {
        this.createCourseUseCase = createCourseUseCase;
        this.inactivateCourseUseCase = inactivateCourseUseCase;
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

    @DeleteMapping("/{code}")
    public ResponseEntity<?> inactivate(@PathVariable String code) {
        inactivateCourseUseCase.execute(code);

        return ResponseEntity.noContent().build();
    }
}
