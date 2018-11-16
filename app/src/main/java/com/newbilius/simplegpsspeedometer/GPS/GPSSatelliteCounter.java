package com.newbilius.simplegpsspeedometer.GPS;

import android.location.GnssStatus;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GPSSatelliteCounter extends GnssStatus.Callback {

    private final IGPSSatelliteCounterCallback counterCallback;

    public GPSSatelliteCounter(IGPSSatelliteCounterCallback counterCallback) {
        this.counterCallback = counterCallback;
    }

    @Override
    public void onSatelliteStatusChanged(GnssStatus status) {
        super.onSatelliteStatusChanged(status);
        counterCallback.setCount(status.getSatelliteCount());
    }
}
