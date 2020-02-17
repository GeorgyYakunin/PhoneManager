package com.fara.phonemanager.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.fara.phonemanager.activities.ActivityBasicAboutDeviceInfo;
import com.fara.phonemanager.activities.ActivityAppIntro;
import com.fara.phonemanager.activities.ActivityBasicGarbageClean;
import com.fara.phonemanager.activities.ActivityBasicMain;
import com.fara.phonemanager.activities.ActivityBasicSaverBattery;
import com.fara.phonemanager.activities.ActivityBasicSplash;
import com.fara.phonemanager.utils.junkclean.DiskStat;
import com.fara.phonemanager.utils.junkclean.MemStat;

/*
 * Displays the route of access to various agents
 * */

public class ActivitiesHelpers {
    private static ActivitiesHelpers activitiesHelpers;
    private Context context;

    public ActivitiesHelpers(Context context) {
        this.context = context;
    }

    public static ActivitiesHelpers getInstance(Context context) {
        if (activitiesHelpers == null)
            activitiesHelpers = new ActivitiesHelpers(context);

        return activitiesHelpers;
    }

    public void gotoActivitySplash() {
        Intent intent = new Intent(this.context, ActivityBasicSplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }

    public void gotoActivityIntro() {
        Intent intent = new Intent(this.context, ActivityAppIntro.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }

    public void gotoActivityMain() {
        final Handler handler = new Handler();

        Thread thread = new Thread(() -> {
            final DiskStat diskStat = new DiskStat();
            final MemStat memStat = new MemStat(context);
            handler.post(() -> {
                Intent intent = new Intent(context, ActivityBasicMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ActivityBasicMain.PARAM_TOTAL_SPACE, diskStat.getTotalSpace());
                intent.putExtra(ActivityBasicMain.PARAM_USED_SPACE, diskStat.getUsedSpace());
                intent.putExtra(ActivityBasicMain.PARAM_TOTAL_MEMORY, memStat.getTotalMemory());
                intent.putExtra(ActivityBasicMain.PARAM_USED_MEMORY, memStat.getUsedMemory());
                context.startActivity(intent);
            });
        });

        thread.start();

//        Intent intent = new Intent(context, ActivityBasicMain.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

    public void gotoActivityJunkClean() {
        context.startActivity(new Intent(context, ActivityBasicGarbageClean.class));
    }

    public void gotoActivitySaveBattery() {
        context.startActivity(new Intent(context, ActivityBasicSaverBattery.class));
    }

    public void gotoActivityDeviceInfo() {
        context.startActivity(new Intent(context, ActivityBasicAboutDeviceInfo.class));
    }

}