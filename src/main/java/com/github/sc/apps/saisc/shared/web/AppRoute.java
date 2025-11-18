package com.github.sc.apps.saisc.shared.web;

/**
 * Central registry of application route paths to avoid cross-feature class references
 * from shared components (like BaseLayout). Keep these values aligned with the
 *
 * @Route annotations defined in the respective view classes.
 */
public enum AppRoute {
    CHAT("chat"),
    CHAT_HISTORY("chat-history"),
    CHAT_MODEL("chat-model"),
    EVENTS("events"),
    HOBBIES("hobbies"),
    PERSONS("persons"),
    DB_CLEAN("db-clean"),
    DUMP("dump");

    private final String path;

    AppRoute(String path) {
        this.path = path;
    }

    /**
     * Returns the base path as defined in the @Route annotation (without leading slash).
     */
    public String path() {
        return path;
    }
}
