package com.mpedroni.aprendatech.infra.courses.api;

import jakarta.validation.constraints.*;

public record CreateCourseRequest(
    @NotBlank(message = "Name is required.")
    String name,

    @NotBlank(message = "Code is required.")
    @Size(max = 10, message = "Code must have at most 10 characters.")
    @Pattern(regexp = "^[a-z-]+$", message = "Code must contain only letters and hyphens.")
    String code,

    @NotNull(message = "Instructor ID is required.")
    Long instructorId,

    @NotBlank(message = "Description is required.")
    String description
) {
}
