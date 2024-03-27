package com.mpedroni.aprendatech.infra.reports.courses.api;

import com.mpedroni.aprendatech.domain.reports.courses.usecases.GetCoursesNpsCommand;
import com.mpedroni.aprendatech.domain.reports.courses.usecases.GetCoursesNpsUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports/courses/nps")
public class CoursesNpsReportController {
    private final GetCoursesNpsUseCase getCoursesNpsUseCase;

    public CoursesNpsReportController(GetCoursesNpsUseCase getCoursesNpsUseCase) {
        this.getCoursesNpsUseCase = getCoursesNpsUseCase;
    }

    @GetMapping
    public Object nps(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer perPage
    ) {
        var aCommand = GetCoursesNpsCommand.with(page, perPage);
        return getCoursesNpsUseCase.execute(aCommand);
    }
}
