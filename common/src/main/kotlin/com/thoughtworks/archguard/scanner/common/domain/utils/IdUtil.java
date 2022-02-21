package com.thoughtworks.archguard.scanner.common.domain.utils;

import java.util.UUID;

public class IdUtil {
    private IdUtil() {
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}
