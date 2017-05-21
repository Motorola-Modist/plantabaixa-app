package com.plantabaixa.android;

import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.plantabaixa.android.sensor.SensorEventListener;
import com.plantabaixa.android.sensor.SonarDistanceSensor;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SonarDistanceSensor sonarDistanceSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sonarDistanceSensor = new SonarDistanceSensor(this);
    }

    @Override
    public void onSensorChanged(com.plantabaixa.android.sensor.SensorEvent sensorEvent) {
        Log.i("TAG", "Segundos: " + sensorEvent.values[0]);
        Log.i("TAG", "Polegadas: " + sensorEvent.values[1]);
        Log.i("TAG", "Centimetros: " + sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {

    }
}
