package com.mpedroni.aprendatech.infra.enrollments.api;

import com.mpedroni.aprendatech.domain.enrollments.usecases.EnrollUserCommand;
import com.mpedroni.aprendatech.domain.enrollments.usecases.EnrollUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollUserUseCase enrollUserUseCase;

    public EnrollmentController(EnrollUserUseCase enrollUserUseCase) {
        this.enrollUserUseCase = enrollUserUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody EnrollUserRequest request,
            Principal principal
    ) {
        var aCommand = EnrollUserCommand.with(request.courseId(), principal.getName());
        enrollUserUseCase.execute(aCommand);

        return ResponseEntity.status(201).build();
    }
}
