package com.newbilius.simplegpsspeedometer.GPS;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.newbilius.simplegpsspeedometer.GPSSpeedCounters.IGPSSpeedCounter;

public class GPSListener implements LocationListener {

    private final IGPSListenerCallback gpsListenerCallback;
    private IGPSSpeedCounter gpsSpeedCounter;
    private float lastSpeed = 0;
    private boolean lastProviderStatus;

    public GPSListener(IGPSListenerCallback gpsListenerCallback) {
        this.gpsListenerCallback = gpsListenerCallback;
    }

    public void setCounter(IGPSSpeedCounter gpsSpeedCounter) {
        this.gpsSpeedCounter = gpsSpeedCounter;
        lastSpeed = 0;
        gpsSpeedCounter.restart();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastSpeed = gpsSpeedCounter.getSpeed(location);
        if (gpsListenerCallback != null)
            gpsListenerCallback.onSpeedChange(lastSpeed);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //не нашёл применения для своих целей
    }

    @Override
    public void onProviderEnabled(String provider) {
        lastProviderStatus = true;
        if (gpsListenerCallback != null)
            gpsListenerCallback.onProviderStatusChanged(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        lastProviderStatus = false;
        if (gpsListenerCallback != null)
            gpsListenerCallback.onProviderStatusChanged(false);
    }
}