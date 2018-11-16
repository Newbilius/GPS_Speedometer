package com.newbilius.simplegpsspeedometer;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.newbilius.simplegpsspeedometer.Utilities.SpeedFormat;

public class DashboardActivityViewModel {

    private final Context context;
    public ObservableField<String> speedText = new ObservableField<>();
    public ObservableBoolean showSpeed = new ObservableBoolean();
    public ObservableField<String> infoText = new ObservableField<>();
    public ObservableBoolean showInfoText = new ObservableBoolean();

    public ObservableBoolean speedInKmH = new ObservableBoolean();
    public ObservableBoolean speedInMlH = new ObservableBoolean();

    private float speed;
    private SpeedFormat speedFormat = SpeedFormat.kmh;

    public DashboardActivityViewModel(Context context) {
        this.context = context;
        setAndShowInfoText("Запуск...");
        speedInKmH.set(true);
    }

    void setSatelliteCount(int count) {
        if (showInfoText.get())
            infoText.set(String.format(context.getString(R.string.satelliteCount), count));
    }

    void setGPSTurnedOff() {
        setAndShowInfoText(context.getString(R.string.gps_turnedOff));
    }

    void setAndShowSpeed(float value) {
        setSpeed(value);
        showInfoText.set(false);
        showSpeed.set(true);
    }

    public void onSpeedFormatChanged() {
        if (speedInKmH.get())
            setSpeedFormat(SpeedFormat.kmh);
        if (speedInMlH.get())
            setSpeedFormat(SpeedFormat.mph);
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
                speedText.set(String.format(context.getString(R.string.speedFormat_mph), Math.round(speed * 2.23694)));
                break;
        }
    }
}
