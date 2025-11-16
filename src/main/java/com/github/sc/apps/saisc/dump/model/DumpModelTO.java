package com.github.sc.apps.saisc.dump.model;

import java.util.List;

public record DumpModelTO(
        List<DumpPersonTO> persons,
        List<DumpEventTO> events,
        List<DumpHobbyTO> hobbies
) {
}
