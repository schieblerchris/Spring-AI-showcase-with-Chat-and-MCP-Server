package com.github.sc.apps.saisc.dump.model;

import java.time.LocalDate;

public record DumpEventTO(
        int id,
        String name,
        LocalDate start,
        LocalDate end,
        int hobbyId,
        String hobby
) {
}
