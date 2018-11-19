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

    public ObservableBoolean speedInKmH = new ObservableBoolean();
    public ObservableBoolean speedInMpH = new ObservableBoolean();

    public ObservableBoolean instantSpeedCounterSelected = new ObservableBoolean();
    public ObservableBoolean medianSpeedCounterSelected = new ObservableBoolean();

    private float speed;
    private SpeedFormat speedFormat = SpeedFormat.kmh;
    private IGPSSpeedCounter gpsSpeedCounter;
    private InstantGPSSpeedCounter instantGPSSpeedCounter;
    private MedianGPSSpeedCounter medianGPSSpeedCounter;

    DashboardActivityViewModel(Context context,
                               AppSettings settings) {
        this.context = context;
        this.settings = settings;
        setAndShowInfoText(context.getString(R.string.satelliteSearch));
        speedInKmH.set(true);
        instantSpeedCounterSelected.set(true);
        instantGPSSpeedCounter = new InstantGPSSpeedCounter();
        medianGPSSpeedCounter = new MedianGPSSpeedCounter(4);
        setCounter(instantGPSSpeedCounter);

        setSpeedFormat(settings.getSpeedFormat());
        setSpeedCounterMode(settings.getSpeedCounterMode());
    }

    private void setSpeedCounterMode(SpeedCounterMode speedCounterMode) {
        settings.saveCounterMode(speedCounterMode);
        instantSpeedCounterSelected.set(speedCounterMode == SpeedCounterMode.Instant);
        medianSpeedCounterSelected.set(speedCounterMode == SpeedCounterMode.Median);

        switch (speedCounterMode) {
            case Instant:
                setCounter(instantGPSSpeedCounter);
                break;
            case Median:
                setCounter(medianGPSSpeedCounter);
                break;
        }
    }

    void setSatelliteCount(int count) {
        if (showInfoText.get())
            infoText.set(String.format(context.getString(R.string.satelliteCount), count));
    }

    void setGPSTurnedOff() {
        setAndShowInfoText(context.getString(R.string.gps_turnedOff));
    }

    void setAndShowSpeed(Location location) {
        if (gpsSpeedCounter != null)
            setSpeed(gpsSpeedCounter.getSpeed(location));
        showInfoText.set(false);
        showSpeed.set(true);
    }

    public void onSpeedFormatChanged() {
        if (speedInKmH.get())
            setSpeedFormat(SpeedFormat.kmh);
        if (speedInMpH.get())
            setSpeedFormat(SpeedFormat.mph);
    }

    public void speedCounterChanged() {
        if (instantSpeedCounterSelected.get())
            setSpeedCounterMode(SpeedCounterMode.Instant);
        if (medianSpeedCounterSelected.get())
            setSpeedCounterMode(SpeedCounterMode.Median);
    }

    private void setCounter(IGPSSpeedCounter gpsSpeedCounter) {
        this.gpsSpeedCounter = gpsSpeedCounter;
        gpsSpeedCounter.restart();
    }

    private void setSpeedFormat(SpeedFormat format) {
        SpeedFormat oldSpeedFormat = speedFormat;
        speedFormat = format;
        settings.saveSpeedFormat(speedFormat);

        speedInKmH.set(format == SpeedFormat.kmh);
        speedInMpH.set(format == SpeedFormat.mph);

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
                speedText.set(String.format(context.getString(R.string.speedFormat_mph), Math.round(speed * 2.23694)));
                break;
        }
    }
}
