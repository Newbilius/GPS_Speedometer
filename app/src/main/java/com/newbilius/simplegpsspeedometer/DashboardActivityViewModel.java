package com.newbilius.simplegpsspeedometer;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.location.Location;

import com.newbilius.simplegpsspeedometer.GPSSpeedCounters.IGPSSpeedCounter;
import com.newbilius.simplegpsspeedometer.GPSSpeedCounters.InstantGPSSpeedCounter;
import com.newbilius.simplegpsspeedometer.GPSSpeedCounters.MedianGPSSpeedCounter;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedCounterMode;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedFormat;

public class DashboardActivityViewModel {

    private final Context context;
    private final AppSettings settings;
    public ObservableField<String> speedText = new ObservableField<>();
    public ObservableBoolean showSpeed = new ObservableBoolean();
    public ObservableField<String> infoText = new ObservableField<>();
    public ObservableBoolean showInfoText = new ObservableBoolean();
    public ObservableBoolean showLoader = new ObservableBoolean();

    private float speed;
    private SpeedFormat speedFormat = SpeedFormat.kmh;
    private IGPSSpeedCounter gpsSpeedCounter;
    private InstantGPSSpeedCounter instantGPSSpeedCounter;
    private MedianGPSSpeedCounter medianGPSSpeedCounter;

    public DashboardActivityViewModel(Context context,
                                      AppSettings settings) {
        this.context = context;
        this.settings = settings;
        setAndShowInfoText(context.getString(R.string.satelliteSearch));
        instantGPSSpeedCounter = new InstantGPSSpeedCounter();
        medianGPSSpeedCounter = new MedianGPSSpeedCounter(4);
        reloadData();
        showLoader.set(true);
    }

    public void reloadData() {
        setSpeedFormat(settings.getSpeedFormat());
        setSpeedCounterMode(settings.getSpeedCounterMode());
    }

    private void setSpeedCounterMode(SpeedCounterMode speedCounterMode) {
        switch (speedCounterMode) {
            case Instant:
                setCounter(instantGPSSpeedCounter);
                break;
            case Median:
                setCounter(medianGPSSpeedCounter);
                break;
        }
    }

    public void setSatelliteCount(int count) {
        if (showInfoText.get()) {
            showLoader.set(true);
            infoText.set(String.format(context.getString(R.string.satelliteCount), count));
        }
    }

    public void setGPSTurnedOff() {
        setAndShowInfoText(context.getString(R.string.gps_turnedOff));
    }

    public void setAndShowSpeed(Location location) {
        if (gpsSpeedCounter != null)
            setSpeed(gpsSpeedCounter.getSpeed(location));
        showInfoText.set(false);
        showLoader.set(false);
        showSpeed.set(true);
    }

    private void setCounter(IGPSSpeedCounter gpsSpeedCounter) {
        this.gpsSpeedCounter = gpsSpeedCounter;
        gpsSpeedCounter.restart();
    }

    private void setSpeedFormat(SpeedFormat format) {
        SpeedFormat oldSpeedFormat = speedFormat;
        speedFormat = format;

        if (oldSpeedFormat != speedFormat)
            setSpeed(speed);
    }

    private void setAndShowInfoText(String text) {
        infoText.set(text);

        showInfoText.set(true);
        showSpeed.set(false);
    }

    private void setSpeed(float value) {
        speed = value;


        switch (speedFormat) {
            case kmh:
                speedText.set(String.format(context.getString(R.string.speedFormat_kmh), Math.round(speed * 3.6)));
                break;
            case mph:
                int speedInMph = (int) Math.round(speed * 2.23694);
                speedText.set(context.getResources().getQuantityString(R.plurals.speedFormat_mph_plurals,
                        speedInMph,
                        speedInMph
                        ));
                break;
        }
    }
}
