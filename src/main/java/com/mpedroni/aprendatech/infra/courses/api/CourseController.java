package com.mpedroni.aprendatech.infra.courses.api;

import com.mpedroni.aprendatech.domain.courses.usecases.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CreateCourseUseCase createCourseUseCase;
    private final InactivateCourseUseCase inactivateCourseUseCase;
    private final SearchCourseUseCase searchCourseUseCase;

    public CourseController(CreateCourseUseCase createCourseUseCase, InactivateCourseUseCase inactivateCourseUseCase, SearchCourseUseCase searchCourseUseCase) {
        this.createCourseUseCase = createCourseUseCase;
        this.inactivateCourseUseCase = inactivateCourseUseCase;
        this.searchCourseUseCase = searchCourseUseCase;
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

    @GetMapping
    public ResponseEntity<?> search(
        @RequestParam(required = false) String status,
        @RequestParam(required = false, defaultValue = "1") Integer page,
        @RequestParam(required = false, defaultValue = "10") Integer perPage
    ) {
        var command = SearchCourseCommand.with(page, perPage, status);
        var result = searchCourseUseCase.execute(command);

        return ResponseEntity.ok(result);
    }
}
