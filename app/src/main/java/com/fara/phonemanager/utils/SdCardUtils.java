package com.fara.phonemanager.utils;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;

/*
* ScanSelectActivity.java
*
* Returns the list of SD cards and memory attached to the phone
* */

public class SdCardUtils {
    public boolean hasStorage(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //External Storage Emulated
            if (Environment.isExternalStorageEmulated()) {
                if (ContextCompat.getExternalFilesDirs(context, null).length > 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public File[] getStorages() {
        return new File("/storage/").listFiles();
    }
}