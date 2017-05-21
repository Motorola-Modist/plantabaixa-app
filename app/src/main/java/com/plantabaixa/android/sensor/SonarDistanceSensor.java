package com.plantabaixa.android.sensor;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by gventura on 20/05/2017.
 */

public class SonarDistanceSensor {
    SensorEventListener listener;

    public SonarDistanceSensor(SensorEventListener listener) {
        this.listener = listener;
        scheduleTrigger();
    }

    private void scheduleTrigger() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList l = getRandomNumbers();
                listener.onSensorChanged(new SensorEvent(new float[]{
                        (float) l.get(0) * 2,
                        (float) l.get(1) * 2,
                        (float) l.get(2) * 2
                }));

                scheduleTrigger();
            }
        }, 1000);
    }

    private ArrayList getRandomNumbers() {
        ArrayList numbersGenerated = new ArrayList();

        for (int i = 0; i < 3; i++) {
            Random randNumber = new Random();
            float iNumber = randNumber.nextFloat() + 1;

            if (!numbersGenerated.contains(iNumber)) {
                numbersGenerated.add(iNumber);
            } else {
                i--;
            }
        }


        return numbersGenerated;
    }


}
