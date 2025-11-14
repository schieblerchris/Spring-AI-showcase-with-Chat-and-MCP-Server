package com.github.sc.apps.saisc.dump;

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
