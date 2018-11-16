package com.newbilius.simplegpsspeedometer.GPS;

public interface IGPSListenerCallback {
    void onSpeedChange(float speed);
    void onProviderStatusChanged(boolean enabled);
}
