package com.mpedroni.aprendatech.domain.feedbacks.usecases;

import java.util.Optional;

public record RegisterFeedbackCommand(
        Long courseId,
        String username,
        Float rating,
        Optional<String> reason
) {
    static public RegisterFeedbackCommand with(Long courseId, String username, Float rating, String reason) {
        return new RegisterFeedbackCommand(courseId, username, rating, Optional.ofNullable(reason));

    }
}
