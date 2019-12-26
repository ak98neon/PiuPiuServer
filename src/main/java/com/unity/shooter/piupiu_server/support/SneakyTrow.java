package com.unity.shooter.piupiu_server.support;

import java.util.concurrent.Callable;

import static java.util.Objects.requireNonNull;

public final class SneakyTrow {
    private SneakyTrow() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Exception> void sneakyThrow(Exception t) throws T {
        throw (T) t;
    }

    public static <T> Callable<T> sneaky(Callable<? extends T> callable) {
        requireNonNull(callable);
        return () -> {
            try {
                return callable.call();
            } catch (final Exception ex) {
                sneakyThrow(new RuntimeException(ex.getMessage()));
            }
            return null;
        };
    }
}
