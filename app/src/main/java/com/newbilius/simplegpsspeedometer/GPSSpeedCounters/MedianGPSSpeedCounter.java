package com.newbilius.simplegpsspeedometer.GPSSpeedCounters;

import android.location.Location;

import com.newbilius.simplegpsspeedometer.Utilities.RingBufferOfFloat;

import java.util.Arrays;

public class MedianGPSSpeedCounter implements IGPSSpeedCounter {

    private final RingBufferOfFloat speedBuffer;

    public MedianGPSSpeedCounter(int secondsForCounting) {
        speedBuffer = new RingBufferOfFloat(secondsForCounting);
    }

    @Override
    public float getSpeed(Location location) {
        speedBuffer.add(location.getSpeed());
        float[] averageSpeedSumBufferArray = speedBuffer.getArray();
        return calculateMedianSpeed(averageSpeedSumBufferArray);
    }

    @Override
    public void restart() {
        speedBuffer.clear();
    }

    private float calculateMedianSpeed(float[] speed) {
        Arrays.sort(speed);
        double median;
        if (speed.length % 2 == 0)
            median = ((double) speed[speed.length / 2] + (double) speed[speed.length / 2 - 1]) / 2;
        else
            median = (double) speed[speed.length / 2];
        return Math.round(median);
    }
}
