package com.plantabaixa.android.sensor;

import android.hardware.Sensor;

/**
 * Created by gventura on 20/05/2017.
 */

public interface SensorEventListener {
    void onSensorChanged(SensorEvent sensorEvent);
    void onAccuracyChanged(Sensor s, int accuracy);
}
