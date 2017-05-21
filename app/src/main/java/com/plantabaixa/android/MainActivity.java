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
                fetchDistance(1);
                setDistance(1, 2);
            }
        });

        btnDimensao2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDistance(1);
                setDistance(2, 2);
            }
        });

        sonarDistanceSensor = new SonarDistanceSensor(this);
    }

    private void fetchDistance(int i) {

    }

    private void setDistance(int i, float distance) {
        switch (i) {
            case 1:
                distancia1 = distance;
                tvDistancia1Metros.setText("99");
                tvDistancia1Centimetros.setText("99");
                btnDimensao1.setVisibility(View.GONE);
                btnDimensao2.setVisibility(View.VISIBLE);
                vMetroQuadradoContainer.setVisibility(View.GONE);
                break;
            case 2:
                distancia2 = distance;
                tvDistancia2Metros.setText("99");
                tvDistancia2Centimetros.setText("99");
                btnDimensao1.setVisibility(View.VISIBLE);
                btnDimensao2.setVisibility(View.GONE);
                calculaMetroQuadrado(distancia1, distancia2);
                break;
        }
    }

    private void calculaMetroQuadrado(float distancia1, float distancia2) {
        tvMetroQuadradoMetros.setText("999");
        tvMetroQuadradoCentimetros.setText("99");
        vMetroQuadradoContainer.setVisibility(View.VISIBLE);

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
