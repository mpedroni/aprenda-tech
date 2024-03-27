package com.mpedroni.aprendatech.infra.feedbacks.api;

import jakarta.validation.constraints.*;

public record RegisterFeedbackRequest(
        @NotNull(message = "Course ID is required.")
        Long courseId,

        @NotNull(message = "Rating is required.")
        @Digits(integer = 2, fraction = 1, message = "Rating must have at most 1 fraction digit.")
        @DecimalMin(value = "0.0", message = "Rating must be greater than or equal to 0.")
        @DecimalMax(value = "10.0", message = "Rating must be less than or equal to 10.")
        Float rating,

        String reason
) {
}
