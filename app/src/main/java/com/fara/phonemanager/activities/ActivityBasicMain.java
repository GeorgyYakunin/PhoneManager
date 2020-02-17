package com.fara.phonemanager.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.an.deviceinfo.permission.PermissionManager;
import com.an.deviceinfo.permission.PermissionUtils;
import com.fara.phonemanager.ActivityBasic;
import com.fara.phonemanager.R;
import com.fara.phonemanager.helpers.ActivitiesHelpers;
import com.fara.phonemanager.listeners.OnSingleClickListener;

import com.skyfishjy.library.RippleBackground;

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
* The main activity, which is the first screen to appear when the user launches the app. In this activity,
* there is a menu button that links to about us activity and share app. Beside that, there are another
* four buttons that point to the device info activity, cleaning files activity, save battery activity and
* safe browser.
* */

public class ActivityBasicMain extends ActivityBasic {
    public static final String PARAM_TOTAL_SPACE = "total_space";
    public static final String PARAM_USED_SPACE = "used_space";
    public static final String PARAM_TOTAL_MEMORY = "total_memory";
    public static final String PARAM_USED_MEMORY = "used_memory";

    private RippleBackground rbRipple;
    private LinearLayout btnScan;
    private LinearLayout btnJunkFiles;
    private LinearLayout btnSaveBattery;
    private LinearLayout btnSafeBrowser;
//    private AdView tbvBanner;


    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        findViews();
        initComponents();
        setViewsListeners();
    }

    @Override
    public void findViews() {
        this.rbRipple = findViewById(R.id.acMain_rbRipple);

        this.btnScan = findViewById(R.id.acMain_btnScan);
        this.btnJunkFiles = findViewById(R.id.acMain_btnJunkFiles);
        this.btnSaveBattery = findViewById(R.id.acMain_btnSaveBattery);
        this.btnSafeBrowser = findViewById(R.id.acMain_btnSafeBrowser);
//        this.tbvBanner = findViewById(R.id.acMain_tbvBanner);
    }

    @Override
    public void initComponents() {
        // To change the color of status bar & navigation bar in accordance with slider background.
        setStatusBarColor(Color.parseColor("#FF093D87"));
        setNavigationBarColor(Color.parseColor("#FF093D87"));
//        initGoogleAdMob();
//        initPopupMenu();

        this.rbRipple.startRippleAnimation();
    }

    @Override
    public void setViewsListeners() {


        this.btnScan.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (ActivityCompat.checkSelfPermission(ActivityBasicMain.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    ActivitiesHelpers.getInstance(ActivityBasicMain.this).gotoActivityDeviceInfo();
                } else {
                    permissionManager = new PermissionManager(ActivityBasicMain.this);
                    requestPermission(Manifest.permission.READ_PHONE_STATE);
                }
            }
        });

        this.btnJunkFiles.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(ActivityBasicMain.this).gotoActivityJunkClean();
            }
        });

        this.btnSaveBattery.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ActivitiesHelpers.getInstance(ActivityBasicMain.this).gotoActivitySaveBattery();
            }
        });

        this.btnSafeBrowser.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent blankIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("about:blank"));
                blankIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(blankIntent);
            }
        });

    }

//    private void initGoogleAdMob() {
//        AdRequest adRequest = new AdRequest.Builder().build();
//        this.tbvBanner.loadAd(adRequest);
//        this.tbvBanner.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//
//            }
//
//            @Override
//            public void onAdOpened() {
//
//            }
//
//            @Override
//            public void onAdClicked() {
//
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//
//            }
//
//            @Override
//            public void onAdClosed() {
//
//            }
//        });
//
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//            }
//        });
//    }

//    private void initPopupMenu() {
//        this.popupMenu = new PopupMenu(ActivityBasicMain.this, this.btnMenu);
//        this.popupMenu.getMenuInflater().inflate(R.menu.textview_popup_menu, this.popupMenu.getMenu());
//
//        Menu menu = popupMenu.getMenu();
//        for (int i = 0; i < menu.size(); i++) {
//            MenuItem mi = menu.getItem(i);
//            mi.setTitle(mi.getTitle());
//        }
//    }

    private void requestPermission(String permission) {
        this.permissionManager.showPermissionDialog(permission)
                .withDenyDialogEnabled(true)
                .withDenyDialogMsg("Phone state permission is required to provide you with the finest information about your device")
                .withCallback(new PermissionManager.PermissionCallback() {
                    @Override
                    public void onPermissionGranted(String[] permissions, int[] grantResults) {
                        ActivitiesHelpers.getInstance(ActivityBasicMain.this).gotoActivityDeviceInfo();
                    }

                    @Override
                    public void onPermissionDismissed(String permission) {
                        /**
                         * user has denied the permission. We can display a custom dialog
                         * to user asking for permission
                         * */
                    }

                    @Override
                    public void onPositiveButtonClicked(DialogInterface dialog, int which) {
                        /**
                         * You can choose to open the
                         * app settings screen
                         * * */
                        PermissionUtils permissionUtils = new PermissionUtils(ActivityBasicMain.this);
                        permissionUtils.openAppSettings();
                    }

                    @Override
                    public void onNegativeButtonClicked(DialogInterface dialog, int which) {
                        /**
                         * The user has denied the permission!
                         * You need to handle this in your code
                         * * */
                    }
                })
                .build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionManager.handleResult(requestCode, permissions, grantResults);
    }
}