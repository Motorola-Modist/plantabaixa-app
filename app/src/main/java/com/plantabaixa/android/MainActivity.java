package com.plantabaixa.android;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.motorola.mod.ModDevice;
import com.motorola.mod.ModManager;
import com.plantabaixa.android.rawdata.UltrasonicSensor;
import com.plantabaixa.android.rawdata.UltrasonicSensorListener;
import com.plantabaixa.android.sensor.SensorEventListener;
import com.plantabaixa.android.sensor.SonarDistanceSensor;

public class MainActivity extends AppCompatActivity implements SensorEventListener, UltrasonicSensorListener {
    private static final int RAW_PERMISSION_REQUEST_CODE = 100;

    private static final int DISTANCE_NONE = 0;
    private static final int DISTANCE_FIRST = 1;
    private static final int DISTANCE_SECOND = 2;

    SonarDistanceSensor sonarDistanceSensor;
    UltrasonicSensor ultrasonicSensor;

    float distancia1, distancia2;

    View pgLoading;

    TextView tvDistancia1Metros;
    TextView tvDistancia1Centimetros;
    TextView tvDistancia2Metros;
    TextView tvDistancia2Centimetros;
    TextView tvMetroQuadradoMetros;
    TextView tvMetroQuadradoCentimetros;

    View vMetroQuadradoContainer;

    Button btnDimensao1;
    Button btnDimensao2;

    int fetchDistanceFlag = DISTANCE_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pgLoading = findViewById(R.id.progress);
        tvDistancia1Metros = (TextView) findViewById(R.id.tv_d1_v1);
        tvDistancia1Centimetros = (TextView) findViewById(R.id.tv_d1_v2);
        tvDistancia2Metros = (TextView) findViewById(R.id.tv_d2_v1);
        tvDistancia2Centimetros = (TextView) findViewById(R.id.tv_d2_v2);
        tvMetroQuadradoMetros = (TextView) findViewById(R.id.tv_m2_d1_v1);
        tvMetroQuadradoCentimetros = (TextView) findViewById(R.id.tv_m2_d1_v2);
        vMetroQuadradoContainer = findViewById(R.id.m2_calc_result);
        btnDimensao1 = (Button) findViewById(R.id.btn_d1);
        btnDimensao2 = (Button) findViewById(R.id.btn_d2);

        btnDimensao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                fetchDistance(DISTANCE_FIRST);
            }
        });

        btnDimensao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoading(true);
                fetchDistance(DISTANCE_SECOND);
            }
        });

        sonarDistanceSensor = new SonarDistanceSensor(this);
    }

    /**
     * Distance is one of {@link #DISTANCE_FIRST} or {@link #DISTANCE_SECOND}.
     *
     * @param distance
     */
    private void fetchDistance(int distance) {
        fetchDistanceFlag = distance;
    }

    private void setDistance(int i, float distance) {
        String[] str = splitDistance(distance);
        switch (i) {
            case DISTANCE_FIRST:
                distancia1 = distance;
                tvDistancia1Metros.setText(str[0]);
                tvDistancia1Centimetros.setText(str[1]);
                btnDimensao1.setVisibility(View.GONE);
                btnDimensao2.setVisibility(View.VISIBLE);

                tvDistancia2Metros.setText("0");
                tvDistancia2Centimetros.setText("0");
                vMetroQuadradoContainer.setVisibility(View.GONE);
                break;
            case MainActivity.DISTANCE_SECOND:
                distancia2 = distance;
                tvDistancia2Metros.setText(str[0]);
                tvDistancia2Centimetros.setText(str[1]);
                btnDimensao1.setVisibility(View.VISIBLE);
                btnDimensao2.setVisibility(View.GONE);
                calculaMetroQuadrado(distancia1, distancia2);
                break;
        }
    }

    private void calculaMetroQuadrado(float distancia1, float distancia2) {
        String[] str = splitDistance(distancia1 * distancia2);
        tvMetroQuadradoMetros.setText(str[0]);
        tvMetroQuadradoCentimetros.setText(str[1]);
        vMetroQuadradoContainer.setVisibility(View.VISIBLE);

    }

    @Override
    public void onSensorChanged(com.plantabaixa.android.sensor.SensorEvent sensorEvent) {
        float seconds = sensorEvent.values[0];
        float inches = sensorEvent.values[1];
        float cm = sensorEvent.values[2];

        Log.i("TAG", "Segundos: " + seconds);
        Log.i("TAG", "Polegadas: " + inches);
        Log.i("TAG", "Centimetros: " + cm);

        if (fetchDistanceFlag != DISTANCE_NONE) {
            // set distance if it was asked
            setDistance(fetchDistanceFlag, cm);
            fetchDistanceFlag = DISTANCE_NONE;
            setLoading(false);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {

    }

    /**
     * Transforms the float 12.3456789 to ["12", "34"]
     *
     * @return
     */
    private String[] splitDistance(float value) {
        String str = String.valueOf(value);
        String[] arr = str.split("\\.");
        // Jeito porco de """arredondar"""
        arr[1] = arr[1].substring(0, 2);
        return arr;
    }

    private void setLoading(boolean loading) {
        if (loading) {
            pgLoading.setVisibility(View.VISIBLE);
        } else {
            pgLoading.setVisibility(View.INVISIBLE);
        }
    }

    //// ULTRASONIC RAW DATA TRANSFER

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
        if (null != ultrasonicSensor) {
            ultrasonicSensor.release();
            ultrasonicSensor = null;
        }
    }

    /**
     * Initial MDK Personality interface
     */
    private void initSensor() {
        if (null == ultrasonicSensor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ultrasonicSensor = new UltrasonicSensor(this, this);
        }
    }

    @Override
    public void onModDeviceAttachmentChanged(ModDevice device) {

    }

    @Override
    public void onFirstResponse(boolean challengePassed) {
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
}
