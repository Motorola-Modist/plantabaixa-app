package com.plantabaixa.android;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.motorola.mod.ModDevice;
import com.motorola.mod.ModManager;
import com.plantabaixa.android.rawdata.TemperatureSensor;
import com.plantabaixa.android.rawdata.TemperatureSensorListener;
import com.plantabaixa.android.rawdata.UltrasonicSensor;
import com.plantabaixa.android.rawdata.UltrasonicSensorListener;

public class RawDataActivity extends AppCompatActivity implements TemperatureSensorListener, UltrasonicSensorListener {
    private static final int RAW_PERMISSION_REQUEST_CODE = 100;

    private TemperatureSensor temperatureSensor;
    private UltrasonicSensor ultrasonicSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        /** Initial MDK Personality interface */
        initSensor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        releaseSensor();
    }

    /**
     * Clean up MDK Personality interface
     */
    private void releaseSensor() {
        /** Clean up MDK Personality interface */
        if (null != temperatureSensor) {
            temperatureSensor.release();
            temperatureSensor = null;
        }
        /** Clean up MDK Personality interface */
        if (null != ultrasonicSensor) {
            ultrasonicSensor.release();
            ultrasonicSensor = null;
        }
    }

    /**
     * Initial MDK Personality interface
     */
    private void initSensor() {
//        if (null == temperatureSensor) {
//            temperatureSensor = new TemperatureSensor(this, this);
//        }
        if (null == ultrasonicSensor) {
            ultrasonicSensor = new UltrasonicSensor(this, this);
        }
    }

    @Override
    public void onModDeviceAttachmentChanged(ModDevice device) {

    }

    @Override
    public void onFirstResponse(boolean challengePassed) {
        temperatureSensor.start(1000);
        ultrasonicSensor.start(1000);
    }

    @Override
    public void onRequestRawPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{ModManager.PERMISSION_USE_RAW_PROTOCOL},
                    RAW_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onDistanceUpdate(double seconds, double inches, double centimeters) {

    }

    /**
     * Handle permission request result
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RAW_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (null != temperatureSensor) {
                    /** Permission grant, try to check RAW I/O of mod device */
                    temperatureSensor.resume();
                }
                if (null != ultrasonicSensor) {
                    /** Permission grant, try to check RAW I/O of mod device */
                    ultrasonicSensor.resume();
                }
            } else {
                // TODO: user declined for RAW accessing permission.
                // You may need pop up a description dialog or other prompts to explain
                // the app cannot work without the permission granted.
            }
        }
    }

    @Override
    public void onTemperatureData(double temperature) {
        Log.i(AppConstants.TAG, "Temperatura: " + temperature);
    }
}
