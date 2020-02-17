package com.fara.phonemanager.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.fara.phonemanager.ActivityBasic;
import com.fara.phonemanager.R;
import com.fara.phonemanager.AppController;
import com.fara.phonemanager.broadcasts.BatteryChangeReceiver;
import com.fara.phonemanager.customviews.WaveLoadingView;
import com.fara.phonemanager.database.daos.BatteryChangeReceiverStatusDao;
import com.fara.phonemanager.database.daos.BatterySettingDao;
import com.fara.phonemanager.database.models.BatteryChangeReceiverStatusModel;
import com.fara.phonemanager.database.models.BatterySettingModel;
import com.fara.phonemanager.dialogs.MessageDialog;
import com.fara.phonemanager.utils.AppUtils;


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
 * This section of its existing home page subsets that requires setting permission access first. This
 * page is aimed at managing the battery level of the phone and alerting the user of its reduction. At
 * first of screen displays the percentage of the phone's battery has been shown. Value of percentage
 * of the phone's battery is obtained from BatteryChangeReceiver's broadcast receiver that has been
 * explained on its class. Beside that, user is able to change the brightness of the screen light without
 * needs going to the settings page. One of the other capabilities is show notification to user and
 * automatically reduce screen brightness when battery level of the phone reach below 30 percentage.
 * */

public class ActivityBasicSaverBattery extends ActivityBasic {
    private WaveLoadingView wlvLoading;
    private TextView tvLeftBrightness;
    private SeekBar sbBrightness;
    private TextView tvRightBrightness;
    private CheckBox cbNotification;
    private CheckBox cbBrightness;

    private boolean isBatteryChangeReceiverRegistered = false;
    private BatteryChangeReceiver batteryChangeReceiver = new BatteryChangeReceiver();

    private final static int CODE_WRITE_SETTINGS_PERMISSION = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_saver_batt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check whether setting permission is grant or not
            if (Settings.System.canWrite(getApplicationContext())) {
                findViews();
                initComponents();
                setViewsListeners();
                registerBatteryChangeReceiver();
            } else {
                // To show dialog to user to grant setting permission
                Bundle bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.app_name));
                bundle.putString("message", getResources().getString(R.string.activity_save_battery_activity_dialog_message_request_setting_permission));

                final MessageDialog messageDialog = new MessageDialog();
                messageDialog.setArguments(bundle);
                messageDialog.show(getSupportFragmentManager(), "Message Dialog");
                messageDialog.setOnClickListeners(() -> requestWriteSettingsPermission());
            }
        } else {
            // check whether setting permission is grant or not
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
                findViews();
                initComponents();
                setViewsListeners();
                registerBatteryChangeReceiver();
            } else {
                // To show dialog to user to grant setting permission
                Bundle bundle = new Bundle();
                bundle.putString("title", getResources().getString(R.string.app_name));
                bundle.putString("message", getResources().getString(R.string.activity_save_battery_activity_dialog_message_request_setting_permission));

                final MessageDialog messageDialog = new MessageDialog();
                messageDialog.setArguments(bundle);
                messageDialog.show(getSupportFragmentManager(), "Message Dialog");
                messageDialog.setOnClickListeners(() -> requestWriteSettingsPermission());
            }
        }
    }

    @Override
    public void findViews() {
        this.wlvLoading = findViewById(R.id.acSaveBattery_wlvLoading);
        this.tvLeftBrightness = findViewById(R.id.acSaveBattery_tvLeftBrightness);
        this.sbBrightness = findViewById(R.id.acSaveBattery_sbBrightness);
        this.tvRightBrightness = findViewById(R.id.acSaveBattery_tvRightBrightness);
        this.cbNotification = findViewById(R.id.acSaveBattery_cbNotification);
        this.cbBrightness = findViewById(R.id.acSaveBattery_cbBrightness);
    }

    @Override
    public void initComponents() {
        // To change the color of status bar & navigation bar in accordance with slider background.
        setStatusBarColor(Color.parseColor("#FF03030e"));
        setNavigationBarColor(Color.parseColor("#FF03030e"));

        // Set battery level of the phone
        this.wlvLoading.setProgressValue(AppController.getDatabase().getBatteryDetailsDao().getLevel());
        this.wlvLoading.setTopTitle(AppController.getDatabase().getBatteryDetailsDao().getLevel() + "%");

        // Seek bar's Brightness
        this.tvLeftBrightness.setText(this.tvLeftBrightness.getText().toString());
        this.sbBrightness.setProgress(new AppUtils().getBrightness(this));
        this.tvRightBrightness.setText(this.tvRightBrightness.getText().toString());

        BatterySettingDao batterySettingDao = AppController.getDatabase().getBatterySettingDao();
        if (batterySettingDao.getSize() > 0) {
            this.cbNotification.setChecked(batterySettingDao.getNotification() == 1);
            this.cbBrightness.setChecked(batterySettingDao.getBrightness() == 1);
        } else {
            this.cbNotification.setChecked(false);
            this.cbBrightness.setChecked(false);
        }


    }

    @Override
    public void setViewsListeners() {
        this.sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new AppUtils().setBrightness(ActivityBasicSaverBattery.this, (progress * 255) / 100);

                try {
                    int br = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.screenBrightness = (float) br / 255;
                    getWindow().setAttributes(lp);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.cbNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            BatterySettingDao dao = AppController.getDatabase().getBatterySettingDao();

            dao.nukeTable();

            BatterySettingModel model = new BatterySettingModel();
            model.setNotification(isChecked);
            model.setBrightness(cbBrightness.isChecked());

            dao.insert(model);
        });

        this.cbBrightness.setOnCheckedChangeListener((buttonView, isChecked) -> {
            BatterySettingDao dao = AppController.getDatabase().getBatterySettingDao();

            dao.nukeTable();

            BatterySettingModel model = new BatterySettingModel();
            model.setBrightness(isChecked);
            model.setNotification(cbNotification.isChecked());

            dao.insert(model);
        });
    }



    private void registerBatteryChangeReceiver() {
        if (!AppController.getDatabase().getBatteryChangeReceiverStatusDao().isBatteryChangeReceiverRegistered()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(getApplicationContext())) {
                    this.isBatteryChangeReceiverRegistered= true;
                    registerReceiver(this.batteryChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                    BatteryChangeReceiverStatusDao batteryChangeReceiverStatusDao = AppController.getDatabase().getBatteryChangeReceiverStatusDao();
                    BatteryChangeReceiverStatusModel batteryChangeReceiverStatusModel = new BatteryChangeReceiverStatusModel();
                    batteryChangeReceiverStatusModel.setBatteryChangeReceiverRegistered(true);
                    batteryChangeReceiverStatusDao.insert(batteryChangeReceiverStatusModel);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
                    this.isBatteryChangeReceiverRegistered= true;
                    registerReceiver(this.batteryChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                    BatteryChangeReceiverStatusDao batteryChangeReceiverStatusDao = AppController.getDatabase().getBatteryChangeReceiverStatusDao();
                    BatteryChangeReceiverStatusModel batteryChangeReceiverStatusModel = new BatteryChangeReceiverStatusModel();
                    batteryChangeReceiverStatusModel.setBatteryChangeReceiverRegistered(true);
                    batteryChangeReceiverStatusDao.insert(batteryChangeReceiverStatusModel);
                }
            }
        }
    }

    private void requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, CODE_WRITE_SETTINGS_PERMISSION);
            } else {
                findViews();
                initComponents();
                setViewsListeners();
                registerBatteryChangeReceiver();
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, CODE_WRITE_SETTINGS_PERMISSION);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION && Settings.System.canWrite(getApplicationContext())) {
            findViews();
            initComponents();
            setViewsListeners();
            registerBatteryChangeReceiver();
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_WRITE_SETTINGS_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            findViews();
            initComponents();
            setViewsListeners();
            registerBatteryChangeReceiver();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
//        if (this.isBatteryChangeReceiverRegistered) {
//            this.isBatteryChangeReceiverRegistered = false;
//            unregisterReceiver(this.batteryChangeReceiver);
//        }

        super.onDestroy();
    }
}