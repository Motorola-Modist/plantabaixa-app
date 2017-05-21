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

public class RawDataActivity extends AppCompatActivity implements TemperatureSensorListener {
    private static final int RAW_PERMISSION_REQUEST_CODE = 100;

    private TemperatureSensor sensor;

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
    public void onDestroy() {
        super.onDestroy();

        releaseSensor();
    }

    /**
     * Clean up MDK Personality interface
     */
    private void releaseSensor() {
        /** Clean up MDK Personality interface */
        if (null != sensor) {
            sensor.release();
            sensor = null;
        }
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

    /**
     * Initial MDK Personality interface
     */
    private void initSensor() {
        if (null == sensor) {
            sensor = new TemperatureSensor(this, this);
        }
    }

    @Override
    public void onModDevice(ModDevice device) {
    }

    @Override
    public void onFirstResponse(boolean challengePassed) {
        sensor.start(1000);
    }

    @Override
    public void onRequestRawPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{ModManager.PERMISSION_USE_RAW_PROTOCOL},
                    RAW_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Handle permission request result
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RAW_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (null != sensor) {
                    /** Permission grant, try to check RAW I/O of mod device */
                    sensor.resume();
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
