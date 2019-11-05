package com.unity.shooter.piupiu_server.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.util.concurrent.TimeUnit;

import static com.unity.shooter.piupiu_server.constants.MetricConst.RESPONSE_TIMER;

public class MetricService {
    private static final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    public MetricService() {
        meterRegistry.timer(RESPONSE_TIMER);
    }

    public void responseTime(long startTime) {
        Timer timer = meterRegistry.timer(RESPONSE_TIMER);
        timer.record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
    }
}
