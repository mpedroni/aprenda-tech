package com.mpedroni.aprendatech.infra.enrollments.api;

import jakarta.validation.constraints.NotNull;

public record EnrollUserRequest(
        @NotNull(message = "Course ID is required.")
        Long courseId
) {
}
