package com.fara.phonemanager.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/*
* ActivityBasicSaverBattery.java
* BatteryChangeReceiver.java
*
*
* Used to adjust the size of the phone's screen light status and change it to the desired screen size
* of the battery. And with this method also operates the auto dimming part of the screen at specified
* percentages.
* */

public class AppUtils {
    public float getCpuTemperature() {
        try {
            Process process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            } else {
                return 51.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public void setBrightness(Context context, int brightness) {
        //constrain the value of brightness
        if (brightness < 0)
            brightness = 0;
        else if (brightness > 255)
            brightness = 255;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context)) {
                ContentResolver cResolver = context.getApplicationContext().getContentResolver();
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
            }
        }
    }

    public int getBrightness(Context context) {
        try {
            int curBrightnessValue = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            return (curBrightnessValue * 100) / 255;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 50;
        }
    }
}