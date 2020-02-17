package com.fara.phonemanager;

import android.content.Context;
import android.content.SharedPreferences;

/*
* This Shared Preferences class to store data in the form of key-value pair.
* */

public class SessionManager {
    private static SessionManager sessionManager;
    private Context context;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private final static String PREFNAME = "com.fara.projects.phone.manager";

    //--------------- Is application run at the first time
    private final static String KEY_IS_APPLICATION_RUN_AT_THE_FIRST_TIME = "is_application_run_at_the_first_time";
    //----- Sharing
    private final static String KEY_SHARING = "sharing";
    //----- Google AdMode Junk File
    private final static String KEY_TPS_JUNK_FILE = "tps_junk_file";
    //----- Google AdMode Save Browser
    private final static String KEY_TPS_SAVE_BROWSER = "tps_save_browser";
    //----- Google AdMode Scan
    private final static String KEY_TPS_SCAN = "tps_scan";
    //----- Last Junk Clean Time
    private final static String KEY_LAST_JUNK_CLEAN_TIME = "last_junk_clean_time";

    private SessionManager(Context context) {
        this.context = context;

        this.preferences = context.getApplicationContext().getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    public static SessionManager getInstance(Context context) {
        if (sessionManager == null)
            sessionManager = new SessionManager(context);

        return sessionManager;
    }

    //--------------- Is application run at the first time

    public void setIsApplicationRunAtTheFirstTime(boolean active) {
        editor.putBoolean(KEY_IS_APPLICATION_RUN_AT_THE_FIRST_TIME, active);
        editor.commit();
    }

    public Boolean isApplicationRunAtTheFirstTime() {
        return preferences.getBoolean(KEY_IS_APPLICATION_RUN_AT_THE_FIRST_TIME, true);
    }

    //----- Sharing

    public void setSharing(String sharing) {
        this.editor.putString(KEY_SHARING, sharing);
        this.editor.commit();
    }

    public String getSharing() {
        return preferences.getString(KEY_SHARING, "");
    }

    //----- Google AdMode Junk File

    public void setTpsJunkFile(int count) {
        this.editor.putInt(KEY_TPS_JUNK_FILE, count);
        this.editor.commit();
    }

    public int getTpsJunkFile() {
        return preferences.getInt(KEY_TPS_JUNK_FILE, 0);
    }

    //----- Google AdMode Save Browser

    public void setTpsSaveBrowser(int count) {
        this.editor.putInt(KEY_TPS_SAVE_BROWSER, count);
        this.editor.commit();
    }

    public int getTpsSaveBrowser() {
        return preferences.getInt(KEY_TPS_SAVE_BROWSER, 0);
    }

    //----- Google AdMode Scan

    public void setTpsScan(int count) {
        this.editor.putInt(KEY_TPS_SCAN, count);
        this.editor.commit();
    }

    public int getTpsScan() {
        return preferences.getInt(KEY_TPS_SCAN, 0);
    }

    //----- Last Junk Clean Time

    public void setLastJunkCleanTime(String nextTime) {
        this.editor.putString(KEY_LAST_JUNK_CLEAN_TIME, nextTime);
        this.editor.commit();
    }

    public String getLastJunkCleanTime() {
        return preferences.getString(KEY_LAST_JUNK_CLEAN_TIME, "");
    }
}