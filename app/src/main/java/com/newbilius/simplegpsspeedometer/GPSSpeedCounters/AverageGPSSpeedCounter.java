package com.newbilius.simplegpsspeedometer.GPSSpeedCounters;

import android.location.Location;

import com.newbilius.simplegpsspeedometer.Utilities.RingBufferOfFloat;

/**
 * Планировал изначально использовать среднюю скорость, но передумал и перешёл на медианную. Класс решил пока не удалять
 */
public class AverageGPSSpeedCounter implements IGPSSpeedCounter {

    private final RingBufferOfFloat speedBuffer;

    public AverageGPSSpeedCounter(int secondsForCounting) {
        speedBuffer = new RingBufferOfFloat(secondsForCounting);
    }

    @Override
    public float getSpeed(Location location) {
        speedBuffer.add(location.getSpeed());
        float[] averageSpeedSumBufferArray = speedBuffer.getArray();

        float averageSpeedSum = 0;
        for (float speedFromBuffer : averageSpeedSumBufferArray)
            averageSpeedSum += speedFromBuffer;

        return averageSpeedSum / averageSpeedSumBufferArray.length;
    }

    @Override
    public void restart() {
        speedBuffer.clear();
    }
}
