package com.fara.phonemanager.helpers;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetHelper {
    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean isInternetOn() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 www.time.ir");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);

            return reachable;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}