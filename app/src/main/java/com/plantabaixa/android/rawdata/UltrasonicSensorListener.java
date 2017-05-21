package com.plantabaixa.android.rawdata;

import com.motorola.mod.ModDevice;

/**
 * Created by gventura on 20/05/2017.
 */

public interface UltrasonicSensorListener {
    void onModDeviceAttachmentChanged(ModDevice device);
    void onFirstResponse(boolean listening);
    void onRequestRawPermission();
    void onDistanceUpdate(double seconds, double inches, double centimeters);
}
