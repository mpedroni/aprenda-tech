package com.mpedroni.aprendatech.domain.reports.courses.usecases;

public record GetCoursesNpsCommand(
        Integer page,
        Integer perPage
) {
    static public GetCoursesNpsCommand with(Integer page, Integer perPage) {
        return new GetCoursesNpsCommand(page, perPage);
    }
}
