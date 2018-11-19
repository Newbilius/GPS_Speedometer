package com.newbilius.simplegpsspeedometer.GPS;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.newbilius.simplegpsspeedometer.GPSSpeedCounters.IGPSSpeedCounter;

public class GPSListener implements LocationListener {

    private final IGPSListenerCallback gpsListenerCallback;

    public GPSListener(IGPSListenerCallback gpsListenerCallback) {
        this.gpsListenerCallback = gpsListenerCallback;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (gpsListenerCallback != null)
            gpsListenerCallback.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //не нашёл применения для своих целей, инфа не всегда актуальна (?)
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (gpsListenerCallback != null)
            gpsListenerCallback.onProviderStatusChanged(true);
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (gpsListenerCallback != null)
            gpsListenerCallback.onProviderStatusChanged(false);
    }
}