package com.fara.phonemanager.broadcasts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.fara.phonemanager.AppController;
import com.fara.phonemanager.R;
import com.fara.phonemanager.activities.ActivityBasicMain;
import com.fara.phonemanager.database.daos.BatteryDetailsDao;
import com.fara.phonemanager.database.daos.BatterySettingDao;
import com.fara.phonemanager.database.models.BatteryDetailsModel;
import com.fara.phonemanager.utils.AppUtils;

/*
 * This Broadcast changes the battery related parameters of the phone including the battery level, captures
 * temperature and voltage instantly as soon as it changes and stores data in the data base after each change
 * when charging or discharging. In this Broadcast, there is a warning of low battery life and a change of screen
 * light, which is repeated at the desired rate of 5% per alarm if the phone is not charging.
 */

public class BatteryChangeReceiver extends BroadcastReceiver {
    private int scale = -1;
    private int level = -1;
    private int voltage = -1;
    private int temp = -1;

    private int mLevel = 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Getting battery details using broadcast that has been declared as battery change and battery low in Manifest
        this.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        this.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        this.temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        this.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

        // To save batter details on Database
        save(this.level, this.scale, this.temp, this.voltage);

        /*
         * Show notification to user and set device screen brightness to 20 provided that user apply related options on
         * save battery activity and beside that device should not on charging.
         */
        if (!isPlugged(context)) {
            if (this.level <= this.mLevel) {
                BatterySettingDao batterySettingDao = AppController.getDatabase().getBatterySettingDao();
                boolean isNotification = batterySettingDao.getNotification() == 1;
                boolean isBrightness = batterySettingDao.getBrightness() == 1;

                if (isNotification)
                    showNotificationOnBatteryLow(context);
                if (isBrightness)
                    new AppUtils().setBrightness(context, 20);

                this.mLevel = this.mLevel - 5;
            }
        } else {
            if (this.level > 30) {
                this.mLevel = 30;
            }
        }
    }

    private void showNotificationOnBatteryLow(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getResources().getString(R.string.receiver_battery_change_notification_context_text))
                .setAutoCancel(true);
        Intent notifyIntent = new Intent(context, ActivityBasicMain.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, notifyIntent, Intent.FILL_IN_ACTION);
        mBuilder.setContentIntent(pi);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void save(int level, int scale, int temp, int voltage) {
        BatteryDetailsDao dao = AppController.getDatabase().getBatteryDetailsDao();

        if (dao.getSize() > 0)
            dao.nukeTable();

        BatteryDetailsModel model = new BatteryDetailsModel();
        model.setLevel(level);
        model.setScale(scale);
        model.setTemperature(temp);
        model.setVoltage(voltage);

        dao.insert(model);
    }

    /*
     * Checking device is under charging or not.
     */
    public boolean isPlugged(Context context) {
        boolean isPlugged= false;
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        isPlugged = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            isPlugged = isPlugged || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        }
        return isPlugged;
    }
}