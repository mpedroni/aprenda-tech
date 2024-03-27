package com.mpedroni.aprendatech.infra.feedbacks.api;

import com.mpedroni.aprendatech.domain.feedbacks.usecases.RegisterFeedbackCommand;
import com.mpedroni.aprendatech.domain.feedbacks.usecases.RegisterFeedbackUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
    private final RegisterFeedbackUseCase registerFeedbackUseCase;

    public FeedbackController(RegisterFeedbackUseCase registerFeedbackUseCase) {
        this.registerFeedbackUseCase = registerFeedbackUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody RegisterFeedbackRequest request,
            Principal principal
    ) {
        var aCommand = RegisterFeedbackCommand.with(
            request.courseId(),
            principal.getName(),
            request.rating(),
            request.reason()
        );
        registerFeedbackUseCase.execute(aCommand);

        return ResponseEntity.status(201).build();
    }
}
