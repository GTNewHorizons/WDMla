package com.gtnewhorizons.wdmla.api;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

import org.jetbrains.annotations.ApiStatus;

/**
 * Minecraft Tick
 * 
 * @see java.time.temporal.ChronoUnit
 */
@ApiStatus.Experimental
public enum Ticks implements TemporalUnit {

    INSTANCE;

    @Override
    public Duration getDuration() {
        return Duration.ofNanos(50000_000);
    }

    /**
     * allow time calculation
     */
    @Override
    public boolean isDurationEstimated() {
        return false;
    }

    @Override
    public boolean isDateBased() {
        return false;
    }

    @Override
    public boolean isTimeBased() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R extends Temporal> R addTo(R temporal, long amount) {
        return (R) temporal.plus(amount, this);
    }

    @Override
    public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
        return temporal1Inclusive.until(temporal2Exclusive, this);
    }

    @Override
    public String toString() {
        return "Ticks";
    }
}
