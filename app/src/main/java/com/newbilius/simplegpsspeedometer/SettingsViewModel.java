package com.newbilius.simplegpsspeedometer;

import android.databinding.ObservableBoolean;

import com.newbilius.simplegpsspeedometer.ApplicationState.AppSettings;
import com.newbilius.simplegpsspeedometer.Utilities.IActivityProvider;
import com.newbilius.simplegpsspeedometer.Utilities.NavigationHelpers;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedCounterMode;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedFormat;

public class SettingsViewModel {
    private final IActivityProvider activityProvider;
    private final AppSettings settings;

    public ObservableBoolean speedInKmH = new ObservableBoolean();
    public ObservableBoolean speedInMpH = new ObservableBoolean();

    public ObservableBoolean instantSpeedCounterSelected = new ObservableBoolean();
    public ObservableBoolean medianSpeedCounterSelected = new ObservableBoolean();

    public SettingsViewModel(IActivityProvider activityProvider, AppSettings settings) {
        this.activityProvider = activityProvider;
        this.settings = settings;

        setSpeedFormat(settings.getSpeedFormat());
        setSpeedCounterMode(settings.getSpeedCounterMode());
    }

    public void onSpeedFormatChanged(int id) {
        switch (id) {
            case R.id.MPHradioButton:
                setSpeedFormat(SpeedFormat.mph);
                break;
            case R.id.KMHradioButton:
                setSpeedFormat(SpeedFormat.kmh);
                break;
        }
    }

    public void speedCounterChanged(int id) {
        switch (id) {
            case R.id.instantSpeedRadioButton:
                setSpeedCounterMode(SpeedCounterMode.Instant);
                break;
            case R.id.medianSpeedRadioButton:
                setSpeedCounterMode(SpeedCounterMode.Median);
                break;
        }
    }

    public void goToOtherApplications() {
        NavigationHelpers.goToUrl(activityProvider.getActivity(), "market://search?q=pub:%D0%94%D0%BC%D0%B8%D1%82%D1%80%D0%B8%D0%B9+%D0%9C%D0%BE%D0%B8%D1%81%D0%B5%D0%B5%D0%B2");
    }

    public void writeReview() {
        NavigationHelpers.rateApplicationInGooglePlay(activityProvider.getActivity());
    }

    public void goToIconAuthorSite() {
        NavigationHelpers.goToUrl(activityProvider.getActivity(), "https://www.iconfinder.com/icons/172557/speedometer_icon");
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