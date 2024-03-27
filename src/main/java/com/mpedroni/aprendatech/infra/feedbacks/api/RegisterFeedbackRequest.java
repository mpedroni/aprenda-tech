package com.mpedroni.aprendatech.infra.feedbacks.api;

public record RegisterFeedbackRequest(
        Long courseId,
        Float rating,
        String reason
) {
}
