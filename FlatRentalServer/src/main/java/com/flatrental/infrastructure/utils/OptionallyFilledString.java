package com.flatrental.infrastructure.utils;

import java.util.Optional;

public class OptionallyFilledString {

    private OptionallyFilledString() {
        throw new AssertionError();
    }

    public static Optional<String> ofEmptyOrFilledString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

}
