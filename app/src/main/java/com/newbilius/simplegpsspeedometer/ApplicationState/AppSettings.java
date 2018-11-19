package com.newbilius.simplegpsspeedometer.ApplicationState;

import com.newbilius.simplegpsspeedometer.Utilities.SharedPreferencesStore;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedCounterMode;
import com.newbilius.simplegpsspeedometer.Utilities.SpeedFormat;

public class AppSettings {

    private final SharedPreferencesStore sharedPreferencesStore;
    private static final String SPEED_FORMAT_KEY = "SpeedFormat";
    private static final String SPEED_COUNTER_MODE_KEY = "SpeedCounterMode";

    public AppSettings(SharedPreferencesStore sharedPreferencesStore) {
        this.sharedPreferencesStore = sharedPreferencesStore;
    }

    public void saveSpeedFormat(SpeedFormat speedFormat) {
        sharedPreferencesStore.setInt(SPEED_FORMAT_KEY, speedFormat.ordinal());
    }

    public SpeedFormat getSpeedFormat() {
        return SpeedFormat.values()[sharedPreferencesStore.getInt(SPEED_FORMAT_KEY, SpeedFormat.kmh.ordinal())];
    }

    public void saveCounterMode(SpeedCounterMode speedFormat) {
        sharedPreferencesStore.setInt(SPEED_COUNTER_MODE_KEY, speedFormat.ordinal());
    }

    public SpeedCounterMode getSpeedCounterMode() {
        return SpeedCounterMode.values()[sharedPreferencesStore.getInt(SPEED_COUNTER_MODE_KEY, SpeedCounterMode.Median.ordinal())];
    }
}
