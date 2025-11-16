package com.github.sc.apps.saisc.dump.model;

import java.time.LocalDate;
import java.util.List;

public record DumpPersonTO(
        int id,
        String firstName,
        String lastName,
        LocalDate birthdate,
        List<SkillTO> skills,
        List<VacationTO> vacations
) {

    public record SkillTO(
            int hobbyId,
            String hobby,
            String skillLevel
    ) {
    }

    public record VacationTO(
            LocalDate start,
            LocalDate end,
            String comment
    ) {
    }
}
