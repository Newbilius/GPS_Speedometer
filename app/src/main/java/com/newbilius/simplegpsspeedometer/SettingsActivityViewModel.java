package com.newbilius.simplegpsspeedometer;

import android.content.Context;
import android.databinding.ObservableBoolean;

import com.newbilius.simplegpsspeedometer.Utilities.NavigationHelpers;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedCounterMode;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedFormat;

public class SettingsActivityViewModel {
    private final Context context;
    private final AppSettings settings;

    public ObservableBoolean speedInKmH = new ObservableBoolean();
    public ObservableBoolean speedInMpH = new ObservableBoolean();

    public ObservableBoolean instantSpeedCounterSelected = new ObservableBoolean();
    public ObservableBoolean medianSpeedCounterSelected = new ObservableBoolean();

    SettingsActivityViewModel(Context context, AppSettings settings) {
        this.context = context;
        this.settings = settings;

        setSpeedFormat(settings.getSpeedFormat());
        setSpeedCounterMode(settings.getSpeedCounterMode());
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

    public void goToOtherApplications() {
        NavigationHelpers.goToUrl(context, "market://search?q=pub:%D0%94%D0%BC%D0%B8%D1%82%D1%80%D0%B8%D0%B9+%D0%9C%D0%BE%D0%B8%D1%81%D0%B5%D0%B5%D0%B2");
    }

    public void writeReview() {
        NavigationHelpers.rateApplicationInGooglePlay(context);
    }

    private void setSpeedCounterMode(SpeedCounterMode speedCounterMode) {
        settings.saveCounterMode(speedCounterMode);
        instantSpeedCounterSelected.set(speedCounterMode == SpeedCounterMode.Instant);
        medianSpeedCounterSelected.set(speedCounterMode == SpeedCounterMode.Median);
    }

    private void setSpeedFormat(SpeedFormat format) {
        settings.saveSpeedFormat(format);
        speedInKmH.set(format == SpeedFormat.kmh);
        speedInMpH.set(format == SpeedFormat.mph);
    }
}