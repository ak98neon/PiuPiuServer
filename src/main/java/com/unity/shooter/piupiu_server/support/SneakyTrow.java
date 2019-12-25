package com.unity.shooter.piupiu_server.support;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public final class SneakyTrow {
    private SneakyTrow() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Exception> void sneakyThrow(Exception t) throws T {
        throw (T) t;
    }

    public static <T> Supplier<T> sneaky(Supplier<T> supplier) {
        requireNonNull(supplier);
        return () -> {
            try {
                return supplier.get();
            } catch (final Exception ex) {
                sneakyThrow(new RuntimeException(ex.getMessage()));
            }
            return null;
        };
    }
}
