package com.fara.phonemanager.utils.junkclean;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

/*
 * For ActivityBasicGarbageClean class
 * */

public class MemStat {
    private long mTotalMemory;
    private long mUsedMemory;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public MemStat(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memInfo);

        this.mTotalMemory = memInfo.totalMem;
        this.mUsedMemory = memInfo.totalMem - memInfo.availMem;
    }

    // Returns the total amount of memory available on the phone
    public long getTotalMemory() {
        return mTotalMemory;
    }

    // Returns the amount of memory used by the phone
    public long getUsedMemory() {
        return mUsedMemory;
    }
}