package com.fara.phonemanager;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.room.Room;

import com.crashlytics.android.Crashlytics;
import com.fara.phonemanager.database.AppDatabase;

import io.fabric.sdk.android.Fabric;

/*
* Base class for maintaining global application state. The Application class in Android is the base
* class within an Android app that contains all other components such as activities and services.
* The Application class, or any subclass of the Application class, is instantiated before any other
* class when the process for your application/package is created.
* */
public class AppController extends Application {
    private static Context context;
    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());

        context = getApplicationContext();
        database = Room.databaseBuilder(this, AppDatabase.class, "onlock").allowMainThreadQueries().build();
    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}