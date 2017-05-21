package com.plantabaixa.android;

import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.plantabaixa.android.sensor.SensorEventListener;
import com.plantabaixa.android.sensor.SonarDistanceSensor;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int DISTANCE_NONE = 0;
    private static final int DISTANCE_FIRST = 1;
    private static final int DISTANCE_SECOND = 2;

    SonarDistanceSensor sonarDistanceSensor;

    float distancia1, distancia2;

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
                fetchDistance(DISTANCE_FIRST);
            }
        });

        btnDimensao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        }
    }

    @Override
    public void onAccuracyChanged(Sensor s, int accuracy) {

    }

    /**
     * Transforms the float 12.3456789 to ["12", "34"]
     * @return
     */
    private String[] splitDistance(float value) {
        String str = String.valueOf(value);
        String[] arr = str.split("\\.");
        // Jeito porco de """arredondar"""
        arr[1] = arr[1].substring(0, 2);
        return arr;
    }
}
