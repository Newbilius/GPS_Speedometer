package com.newbilius.simplegpsspeedometer.GPSSpeedCounters;

import android.location.Location;

public class InstantGPSSpeedCounter implements IGPSSpeedCounter {
    @Override
    public float getSpeed(Location location) {
        return location.getSpeed();
    }

    @Override
    public void restart() {
    }
}