package com.fara.phonemanager.broadcasts;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.fara.phonemanager.AppController;
import com.fara.phonemanager.database.daos.BatteryChangeReceiverStatusDao;
import com.fara.phonemanager.database.models.BatteryChangeReceiverStatusModel;

/*
 * This Broadcast works independently of the app, and when the phone is booting, it is constantly
 * active on the phone memory. This service is used for two purposes only. One for executing program
 * services and the other for related broadcasts without having to run the program. Here we run Battery
 * Changer Receiver More details are provided in the Battery Broadcast section itself/
 * */
public class autostart extends BroadcastReceiver {
    private BatteryChangeReceiver batteryChangeReceiver = new BatteryChangeReceiver();

    public void onReceive(Context context, Intent intent) {
        registerBatteryChangeReceiver(context);
    }

    private void registerBatteryChangeReceiver(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context.getApplicationContext())) {
                context.getApplicationContext().registerReceiver(this.batteryChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                BatteryChangeReceiverStatusDao batteryChangeReceiverStatusDao = AppController.getDatabase().getBatteryChangeReceiverStatusDao();
                BatteryChangeReceiverStatusModel batteryChangeReceiverStatusModel = new BatteryChangeReceiverStatusModel();
                batteryChangeReceiverStatusModel.setBatteryChangeReceiverRegistered(true);
                batteryChangeReceiverStatusDao.insert(batteryChangeReceiverStatusModel);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
                context.getApplicationContext().registerReceiver(this.batteryChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                BatteryChangeReceiverStatusDao batteryChangeReceiverStatusDao = AppController.getDatabase().getBatteryChangeReceiverStatusDao();
                BatteryChangeReceiverStatusModel batteryChangeReceiverStatusModel = new BatteryChangeReceiverStatusModel();
                batteryChangeReceiverStatusModel.setBatteryChangeReceiverRegistered(true);
                batteryChangeReceiverStatusDao.insert(batteryChangeReceiverStatusModel);
            }
        }
    }
}