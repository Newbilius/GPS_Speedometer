package com.newbilius.simplegpsspeedometer.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.newbilius.simplegpsspeedometer.ApplicationState.AppSettings;
import com.newbilius.simplegpsspeedometer.DashboardViewModel;
import com.newbilius.simplegpsspeedometer.GPS.GPSListener;
import com.newbilius.simplegpsspeedometer.GPS.GPSSatelliteCounter;
import com.newbilius.simplegpsspeedometer.GPS.IGPSListenerCallback;
import com.newbilius.simplegpsspeedometer.GPS.IGPSSatelliteCounterCallback;
import com.newbilius.simplegpsspeedometer.ApplicationState.MainApplication;
import com.newbilius.simplegpsspeedometer.R;
import com.newbilius.simplegpsspeedometer.Utilities.IActivityProvider;
import com.newbilius.simplegpsspeedometer.Utilities.SharedPreferencesStore;
import com.newbilius.simplegpsspeedometer.databinding.ActivityDashboardBinding;

//todo разбивка модели на ViewModel + Model ?

//todo иконка
//todo скриншоты
//todo readme на гитхаб

public class DashboardActivity extends AppCompatActivity implements IActivityProvider {

    private LocationManager locationManager;

    private static final int requestGpsPermissionsCallback = 4242;
    private GPSListener gpsListener;
    private DashboardViewModel model;
    private ActivityDashboardBinding binding;

    private GPSSatelliteCounter gpsSatelliteCounter;
    private AlertDialog needGPSTurnOnDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        model = new DashboardViewModel(this, new AppSettings(new SharedPreferencesStore(MainApplication.getAppContext())));
        binding.setModel(model);

        initListeners();
    }

    private void initListeners() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsListener = new GPSListener(new IGPSListenerCallback() {
            @Override
            public void onLocationChanged(Location location) {
                model.setAndShowSpeed(location);
            }

            @Override
            public void onProviderStatusChanged(boolean enabled) {
                if (!enabled) {
                    model.setGPSTurnedOff();
                    turnOnGPSDialog();
                } else
                    dismissTurnOnGPSDialog();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            gpsSatelliteCounter = new GPSSatelliteCounter(new IGPSSatelliteCounterCallback() {
                @Override
                public void setCount(int count) {
                    model.setSatelliteCount(count);
                }
            });
    }

    private void turnOnGPSDialog() {
        dismissTurnOnGPSDialog();

        needGPSTurnOnDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.noGPS_title)
                .setMessage(R.string.noGPS_text)
                .setPositiveButton(R.string.noGPS_turnOn_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.noGPS_exit_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (!model.isGPSActivated())
                            finish();
                    }
                })
                .show();
    }

    private void dismissTurnOnGPSDialog() {
        if (needGPSTurnOnDialog != null && needGPSTurnOnDialog.isShowing())
            needGPSTurnOnDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        model.reloadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(gpsListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            locationManager.unregisterGnssStatusCallback(gpsSatelliteCounter);
        binding.invalidateAll();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean success = true;

        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                success = false;
                break;
            }
        }
        if (success)
            requestPermissions();
        else
            showAlertAboutPermissions();
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates("gps", 1000, 0, gpsListener);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                locationManager.registerGnssStatusCallback(gpsSatelliteCounter);

        } else ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, requestGpsPermissionsCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
            SettingsActivity.startActivity(this);
        return super.onOptionsItemSelected(item);
    }

    private void showAlertAboutPermissions() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.noPermissions_title)
                .setMessage(R.string.noPermissions_text)
                .setPositiveButton(R.string.noPermissions_turnOn_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions();
                    }
                })
                .setNegativeButton(R.string.noPermissions_exit_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
