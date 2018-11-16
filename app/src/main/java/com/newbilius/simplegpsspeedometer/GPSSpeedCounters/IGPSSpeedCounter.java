package com.newbilius.simplegpsspeedometer.GPSSpeedCounters;

import android.location.Location;

public interface IGPSSpeedCounter {
    /**
     * @param location
     * @return speed in m/s
     */
    float getSpeed(Location location);

    /**
     * Start counter from the beginning
     */
    void restart();
}