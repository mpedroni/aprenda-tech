package com.mpedroni.aprendatech.domain.courses.usecases;

import java.util.Optional;

public record SearchCourseCommand(
        Integer page,
        Integer perPage,
        Optional<String> status
) {
    public static SearchCourseCommand with(Integer page, Integer perPage, String status) {
        return new SearchCourseCommand(page, perPage, Optional.ofNullable(status));
    }

    public static SearchCourseCommand with(Integer page, Integer perPage) {
        return new SearchCourseCommand(page, perPage, Optional.empty());
    }
}
