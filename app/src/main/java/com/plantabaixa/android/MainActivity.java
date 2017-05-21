package com.plantabaixa.android;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.plantabaixa.android.sensor.SensorEventListener;
import com.plantabaixa.android.sensor.SonarDistanceSensor;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SonarDistanceSensor sonarDistanceSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRaw = (Button) findViewById(R.id.btn_raw_data);
        btnRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    startActivity(new Intent(MainActivity.this, RawDataActivity.class));
                }
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            btnRaw.setEnabled(false);
        }

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
