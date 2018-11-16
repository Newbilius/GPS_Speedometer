package com.newbilius.simplegpsspeedometer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.newbilius.simplegpsspeedometer.GPS.GPSListener;
import com.newbilius.simplegpsspeedometer.GPS.GPSSatelliteCounter;
import com.newbilius.simplegpsspeedometer.GPS.IGPSListenerCallback;
import com.newbilius.simplegpsspeedometer.GPS.IGPSSatelliteCounterCallback;
import com.newbilius.simplegpsspeedometer.GPSSpeedCounters.InstantGPSSpeedCounter;
import com.newbilius.simplegpsspeedometer.GPSSpeedCounters.MedianGPSSpeedCounter;
import com.newbilius.simplegpsspeedometer.databinding.ActivityDashboardBinding;

//todo сохранение настроек
//todo подправить дизайн
//todo экран "об авторе"
//todo вынести настройки на отдельный экран/модальное окно (?)
//todo сбалансировать, что запихну
//todo локализация на английском
//todo иконка
//todo скриншоты
//todo readme
//todo сплэш (?)
//todo разделить модель и вьюмодель ?
//todo кто должен отвечать за подстановку нужной считалки (instantGPSSpeedCounter / medianGPSSpeedCounter) ?

public class DashboardActivity extends AppCompatActivity {

    private LocationManager locationManager;

    private static final int requestGpsPermissionsCallback = 4242;
    private GPSListener gpsListener;
    private DashboardActivityViewModel model;
    private ActivityDashboardBinding binding;

    private InstantGPSSpeedCounter instantGPSSpeedCounter;
    private MedianGPSSpeedCounter medianGPSSpeedCounter;
    private GPSSatelliteCounter gpsSatelliteCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        model = new DashboardActivityViewModel(this);
        binding.setModel(model);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsListener = new GPSListener(new IGPSListenerCallback() {
            @Override
            public void onSpeedChange(float speed) {
                model.setAndShowSpeed(speed);
            }

            @Override
            public void onProviderStatusChanged(boolean enabled) {
                if (!enabled) {
                    model.setGPSTurnedOff();
                    turnOnGPSMessage();
                }
            }
        });

        instantGPSSpeedCounter = new InstantGPSSpeedCounter();
        medianGPSSpeedCounter = new MedianGPSSpeedCounter(4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            gpsSatelliteCounter = new GPSSatelliteCounter(new IGPSSatelliteCounterCallback() {
                @Override
                public void setCount(int count) {
                    model.setSatelliteCount(count);
                }
            });
        gpsListener.setCounter(medianGPSSpeedCounter);
    }

    private void turnOnGPSMessage() {
        new AlertDialog.Builder(this)
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
                        finish();
                    }
                })
                .show();

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
                .show();
    }
}
