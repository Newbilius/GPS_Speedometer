package com.newbilius.simplegpsspeedometer.Activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.newbilius.simplegpsspeedometer.ApplicationState.AppSettings;
import com.newbilius.simplegpsspeedometer.ApplicationState.MainApplication;
import com.newbilius.simplegpsspeedometer.R;
import com.newbilius.simplegpsspeedometer.SettingsViewModel;
import com.newbilius.simplegpsspeedometer.Utilities.IActivityProvider;
import com.newbilius.simplegpsspeedometer.Utilities.SharedPreferencesStore;
import com.newbilius.simplegpsspeedometer.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements IActivityProvider {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        SettingsViewModel model = new SettingsViewModel(this,
                new AppSettings(new SharedPreferencesStore(MainApplication.getAppContext())));
        binding.setModel(model);
        setTitle(R.string.settingsScreenTitle);
    }

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
