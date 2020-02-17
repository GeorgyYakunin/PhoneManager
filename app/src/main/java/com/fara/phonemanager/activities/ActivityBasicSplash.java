package com.fara.phonemanager.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.fara.phonemanager.AppController;
import com.fara.phonemanager.ActivityBasic;
import com.fara.phonemanager.R;
import com.fara.phonemanager.SessionManager;
import com.fara.phonemanager.database.daos.AdMobDao;
import com.fara.phonemanager.database.daos.BatteryDetailsDao;
import com.fara.phonemanager.database.models.BatteryDetailsModel;
import com.fara.phonemanager.database.models.AdMobModel;
import com.fara.phonemanager.helpers.ActivitiesHelpers;

/*
 * It should be noted that all activities are extended from ActivityBasic. It contain three abstract
 * methods that have been declared to provides implementations on every activity. These methods are
 * mentioned as below :
 *
 * findViews() - To finds the view from the layout resource file that are attached with current Activity.
 * initComponents() - To initialize all views and variables related to current activity.
 * setViewsListeners() - Just to implement your view event listener.
 *
 * In addition to there are some methods are declared on ActivityBasic to perform a specific task that
 * help to you for programming speed and shorthand. Such as show and hide keyboard, show message dialog,
 * show custom alert, toast and snackbar and so on. Take a look at ActivityBasic to see all methods and
 * its descriptions.
 * */

/*
 * This section is enabled after the episode is accessed. The required data is read from the database
 * which includes battery detail information, The scenario is how the in-app ads are displayed and the
 * date saved as the latest update. The first time the app runs after the splash, we go to the intro
 * screen otherwise, after splashing, it will enter the app's home screen.
 * */
public class ActivityBasicSplash extends ActivityBasic {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);

        findViews();
        initComponents();
        setViewsListeners();
    }

    @Override
    public void findViews() {

    }

    @Override
    public void initComponents() {
        setStatusBarColor(Color.parseColor("#0d47a1"));
        setNavigationBarColor(Color.parseColor("#0d47a1"));


        if (SessionManager.getInstance(ActivityBasicSplash.this).getTpsScan() > 200) {
            SessionManager.getInstance(ActivityBasicSplash.this).setTpsScan(0);
        }

        SessionManager.getInstance(ActivityBasicSplash.this).setSharing("Your sharing app content here");

        insertBatteryDetails();
        initAdMobDatabase();
        gotoNextActivity();
    }

    @Override
    public void setViewsListeners() {

    }

    // Insert battery details on database
    private void insertBatteryDetails() {
        BatteryDetailsDao dao = AppController.getDatabase().getBatteryDetailsDao();
        if (dao.getSize() > 0) {
            BatteryDetailsModel model = new BatteryDetailsModel();
            model.setLevel(0);
            model.setScale(0);
            model.setTemperature(0);
            model.setVoltage(0);

            dao.insert(model);
        }
    }

    // Advertising scenario
    private void initAdMobDatabase() {
        AdMobDao adMobDao = AppController.getDatabase().getTapSellDAO();
        AdMobModel adMobModel = new AdMobModel();
        adMobModel.setAdmobJunkFile(1);
        adMobModel.setAdmobSaveBrowser(1);
        adMobModel.setAdmobScan(1);
        adMobDao.insert(adMobModel);
    }

    private void gotoNextActivity() {
        new Handler().postDelayed(() -> {
            if (SessionManager.getInstance(ActivityBasicSplash.this).isApplicationRunAtTheFirstTime()) {
                ActivitiesHelpers.getInstance(ActivityBasicSplash.this).gotoActivityIntro();
                SessionManager.getInstance(ActivityBasicSplash.this).setIsApplicationRunAtTheFirstTime(false);
            } else {
                ActivitiesHelpers.getInstance(ActivityBasicSplash.this).gotoActivityMain();
            }
        }, 7000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}