package com.fara.phonemanager.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.an.deviceinfo.device.model.Battery;
import com.an.deviceinfo.device.model.Device;
import com.an.deviceinfo.device.model.Memory;
import com.an.deviceinfo.device.model.Network;
import com.an.deviceinfo.userapps.UserAppInfo;
import com.an.deviceinfo.userapps.UserApps;
import com.fara.phonemanager.ActivityBasic;
import com.fara.phonemanager.R;
import com.fara.phonemanager.adapters.DeviceInfoAdapter;
import com.fara.phonemanager.adapters.UserAppsAdapter;
import com.fara.phonemanager.info.DeviceInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

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
 * This activity has been used to getting device info and display them to user as tab bar. Device
 * information can be consist as below :
 *  - Device info
 *  - Screen info
 *  - Memory info
 *  - Network info
 *  - User apps
 *  - CPU info
 *  - Battery Info
 *
 * For more information of this activity, refer to following these links
 *
 *      https://github.com/anitaa1990/DeviceInfo-Sample
 *      https://github.com/Devlight/NavigationTabBar
 * */

public class ActivityBasicAboutDeviceInfo extends ActivityBasic {
    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;
    private String[] colors;
    private ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

    private List<DeviceInfo> listDeviceInfo = new ArrayList<>();
    private List<DeviceInfo> listScreenInfo = new ArrayList<>();
    private List<DeviceInfo> listMemoryInfo = new ArrayList<>();
    private List<DeviceInfo> listNetworkInfo = new ArrayList<>();
    private List<UserApps> listUserApps = new ArrayList<>();
    private List<DeviceInfo> listCpuInfo = new ArrayList<>();
    private List<DeviceInfo> listBatteryInfo = new ArrayList<>();

    private Device device;
    private Memory memory;
    private Network network;
    private UserAppInfo userAppInfo;
    private List<UserApps> userApps;
    private Battery battery;

    private DeviceInfoAdapter deviceInfoAdapter;
    private DeviceInfoAdapter screenInfoAdapter;
    private DeviceInfoAdapter memoryInfoAdapter;
    private DeviceInfoAdapter networkInfoAdapter;
    private UserAppsAdapter userAppsAdapter;
    private DeviceInfoAdapter cpuInfoAdapter;
    private DeviceInfoAdapter batteryInfoAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_about_device_info);

        findViews();
        initComponents();
        setViewsListeners();
    }

    @Override
    public void findViews() {
        this.viewPager = findViewById(R.id.vp_vertical_ntb);
        this.navigationTabBar = findViewById(R.id.ntb_vertical);
    }

    @Override
    public void initComponents() {
        setStatusBarColor(Color.parseColor("#FF000000"));
        setNavigationBarColor(Color.parseColor("#FF000000"));

        this.colors = getResources().getStringArray(R.array.vertical_ntb);

        this.device = new Device(this);
        this.memory = new Memory(this);
        this.network = new Network(this);
        this.userAppInfo = new UserAppInfo(this);
        this.userApps = this.userAppInfo.getInstalledApps(false);
        this.battery = new Battery(this);

        initListDeviceInfo();
        initScreenInfo();
        initMemoryInfo();
        initNetworkInfo();
        initUserApps();
        initCpuInfo();
        initBatteryInfo();

        this.deviceInfoAdapter = new DeviceInfoAdapter(this.listDeviceInfo);
        this.screenInfoAdapter = new DeviceInfoAdapter(this.listScreenInfo);
        this.memoryInfoAdapter = new DeviceInfoAdapter(this.listMemoryInfo);
        this.networkInfoAdapter = new DeviceInfoAdapter(this.listNetworkInfo);
        this.userAppsAdapter = new UserAppsAdapter(this.listUserApps);
        this.cpuInfoAdapter = new DeviceInfoAdapter(this.listCpuInfo);
        this.batteryInfoAdapter = new DeviceInfoAdapter(this.listBatteryInfo);

        this.mLayoutManager = new LinearLayoutManager(this);
        this.mLayoutManager.setOrientation(RecyclerView.VERTICAL);

        initViewPagerAdapter();
        initNavigationTabBarModel();
        initNavigationTabBar();
    }

    @Override
    public void setViewsListeners() {

    }

    private void initListDeviceInfo() {
        this.listDeviceInfo.add(new DeviceInfo("Release Build Version", device.getReleaseBuildVersion()));
        this.listDeviceInfo.add(new DeviceInfo("Version Code Name", device.getBuildVersionCodeName()));
        this.listDeviceInfo.add(new DeviceInfo("Manufacturer", device.getManufacturer()));
        this.listDeviceInfo.add(new DeviceInfo("Model", device.getModel()));
        this.listDeviceInfo.add(new DeviceInfo("Product", device.getProduct()));
        this.listDeviceInfo.add(new DeviceInfo("Fingerprint", device.getFingerprint()));
        this.listDeviceInfo.add(new DeviceInfo("Hardware", device.getHardware()));
        this.listDeviceInfo.add(new DeviceInfo("Radio Version", device.getRadioVersion()));
        this.listDeviceInfo.add(new DeviceInfo("Device", device.getDevice()));
        this.listDeviceInfo.add(new DeviceInfo("Board", device.getBoard()));
        this.listDeviceInfo.add(new DeviceInfo("Display Version", device.getDisplayVersion()));
        this.listDeviceInfo.add(new DeviceInfo("Build Brand", device.getBuildBrand()));
        this.listDeviceInfo.add(new DeviceInfo("Build Host", device.getBuildHost()));
        this.listDeviceInfo.add(new DeviceInfo("Build Time", String.valueOf(device.getBuildTime())));
        this.listDeviceInfo.add(new DeviceInfo("Build User", String.valueOf(device.getBuildUser())));
        this.listDeviceInfo.add(new DeviceInfo("Serial", device.getSerial()));
        this.listDeviceInfo.add(new DeviceInfo("Os Version", device.getOsVersion()));
        this.listDeviceInfo.add(new DeviceInfo("Language", device.getLanguage()));
        this.listDeviceInfo.add(new DeviceInfo("SDK Version", String.valueOf(device.getSdkVersion())));
    }

    private void initScreenInfo() {
        try {
            this.listScreenInfo.add(new DeviceInfo("Density", device.getScreenDensity()));
            this.listScreenInfo.add(new DeviceInfo("Resolution", device.getScreenWidth() + " X " + device.getScreenHeight()));
            this.listScreenInfo.add(new DeviceInfo("Brightness", String.valueOf(android.provider.Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS) / 100.0f)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initMemoryInfo() {
        try {
            this.listMemoryInfo.add(new DeviceInfo("Has sd Card", String.valueOf(memory.isHasExternalSDCard())));
            this.listMemoryInfo.add(new DeviceInfo("Total RAM", (String.format("%.2f", memory.getTotalRAM() / 1073741824.0f)) + " GB"));
            this.listMemoryInfo.add(new DeviceInfo("Available Internal Memory Size", String.format("%.2f", (memory.getAvailableInternalMemorySize() / 1073741824.0f)) + " GB"));
            this.listMemoryInfo.add(new DeviceInfo("Total Internal Memory Size", String.format("%.2f", (memory.getTotalInternalMemorySize() / 1073741824.0f)) + " GB"));
            this.listMemoryInfo.add(new DeviceInfo("Available External Memory Size", String.format("%.2f", (memory.getAvailableExternalMemorySize() / 1073741824.0f)) + " GB"));
            this.listMemoryInfo.add(new DeviceInfo("Total External Memory Size", String.format("%.2f", (memory.getTotalExternalMemorySize() / 1073741824.0f)) + " GB"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initNetworkInfo() {
        try {
            this.listNetworkInfo.add(new DeviceInfo("IMEI", network.getIMEI()));
            this.listNetworkInfo.add(new DeviceInfo("IMSI", network.getIMSI()));
            this.listNetworkInfo.add(new DeviceInfo("Phone Type", network.getPhoneType()));
            this.listNetworkInfo.add(new DeviceInfo("Phone Number", network.getPhoneNumber()));
            this.listNetworkInfo.add(new DeviceInfo("Operator", network.getOperator()));
            this.listNetworkInfo.add(new DeviceInfo("SIM Serial", network.getsIMSerial()));
            this.listNetworkInfo.add(new DeviceInfo("Network Class", network.getNetworkClass()));
            this.listNetworkInfo.add(new DeviceInfo("Network Type", network.getNetworkType()));
            this.listNetworkInfo.add(new DeviceInfo("Is SIM Locked", String.valueOf(network.isSimNetworkLocked())));
            this.listNetworkInfo.add(new DeviceInfo("Is Nfc Present", String.valueOf(network.isNfcPresent())));
            this.listNetworkInfo.add(new DeviceInfo("Is Nfc Enabled", String.valueOf(network.isNfcEnabled())));
            this.listNetworkInfo.add(new DeviceInfo("Is Wifi Enabled", String.valueOf(network.isWifiEnabled())));
            this.listNetworkInfo.add(new DeviceInfo("Is Network Available", String.valueOf(network.isNetworkAvailable())));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initUserApps() {
        try {
            for (UserApps userApp : userApps) {
                this.listUserApps.add(userApp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initCpuInfo() {
        try {
            String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
            ProcessBuilder processBuilder = new ProcessBuilder(DATA);
            Process process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            byte[] byteArry = new byte[1024];
            String output = "";
            while (inputStream.read(byteArry) != -1) {
                output = output + new String(byteArry);
            }
            inputStream.close();

            String[] lines = output.split(System.getProperty("line.separator"));
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains(":")) {
                    String[] line = lines[i].split(":");
                    this.listCpuInfo.add(new DeviceInfo(line[0], line[1]));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initBatteryInfo() {
        this.listBatteryInfo.add(new DeviceInfo("Battery Percent", String.valueOf(battery.getBatteryPercent())));
        this.listBatteryInfo.add(new DeviceInfo("Is Phone Charging", String.valueOf(battery.isPhoneCharging())));
        this.listBatteryInfo.add(new DeviceInfo("Battery Health", String.valueOf(battery.getBatteryHealth())));
        this.listBatteryInfo.add(new DeviceInfo("Battery Technology", String.valueOf(battery.getBatteryTechnology())));
        this.listBatteryInfo.add(new DeviceInfo("Battery Temperature", String.valueOf(battery.getBatteryTemperature())));
        this.listBatteryInfo.add(new DeviceInfo("Battery Voltage", String.valueOf(battery.getBatteryVoltage())));
        this.listBatteryInfo.add(new DeviceInfo("Charging Source", String.valueOf(battery.getChargingSource())));
        this.listBatteryInfo.add(new DeviceInfo("Is Battery Present", String.valueOf(battery.isBatteryPresent())));
    }

    private void initViewPagerAdapter() {
        this.viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 7;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                if (position == 0) {
                    final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frag_about_device_info, null, false);
                    initDeviceInfo(view);
                    container.addView(view);
                    return view;
                } else if (position == 1) {
                    final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frag_about_device_info, null, false);
                    initScreenInfo(view);
                    container.addView(view);
                    return view;
                } else if (position == 2) {
                    final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frag_about_device_info, null, false);
                    initMemoryInfo(view);
                    container.addView(view);
                    return view;
                } else if (position == 3) {
                    final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frag_about_device_info, null, false);
                    initNetworkInfo(view);
                    container.addView(view);
                    return view;
                } else if (position == 4) {
                    final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frag_about_device_info, null, false);
                    initAppsInfo(view);
                    container.addView(view);
                    return view;
                } else if (position == 5) {
                    final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frag_about_device_info, null, false);
                    initCpuInfo(view);
                    container.addView(view);
                    return view;
                } else {
                    final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frag_about_device_info, null, false);
                    initBatteryInfo(view);
                    container.addView(view);
                    return view;
                }
            }
        });
    }

    private void initNavigationTabBarModel() {
        this.models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_device_info_icon_device),
                        Color.parseColor(this.colors[0])
                ).title("Heart")
                        .badgeTitle("NTB")
                        .build()
        );

        this.models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_device_info_icon_screen),
                        Color.parseColor(this.colors[1])
                ).title("Cup")
                        .badgeTitle("with")
                        .build()
        );

        this.models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_device_info_icon_memory),
                        Color.parseColor(this.colors[2])
                ).title("Diploma")
                        .badgeTitle("state")
                        .build()
        );

        this.models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_device_info_icon_network),
                        Color.parseColor(this.colors[3])
                ).title("Flag")
                        .badgeTitle("icon")
                        .build()
        );

        this.models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_device_info_icon_apps),
                        Color.parseColor(this.colors[4])
                ).title("Medal")
                        .badgeTitle("777")
                        .build()
        );

        this.models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_device_info_icon_cpu),
                        Color.parseColor(this.colors[5])
                ).title("Medal")
                        .badgeTitle("777")
                        .build()
        );

        this.models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_device_info_icon_battery),
                        Color.parseColor(this.colors[6])
                ).title("Medal")
                        .badgeTitle("777")
                        .build()
        );
    }

    private void initNavigationTabBar() {
        this.navigationTabBar.setModels(this.models);
        this.navigationTabBar.setViewPager(this.viewPager, 0);

        this.navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        this.navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        this.navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        this.navigationTabBar.setIsBadged(true);
        this.navigationTabBar.setIsTitled(true);
        this.navigationTabBar.setIsTinted(true);
        this.navigationTabBar.setIsBadgeUseTypeface(true);
        this.navigationTabBar.setBadgeBgColor(Color.RED);
        this.navigationTabBar.setBadgeTitleColor(Color.WHITE);
        this.navigationTabBar.setIsSwiped(true);
        this.navigationTabBar.setBgColor(Color.BLACK);
        this.navigationTabBar.setBadgeSize(10);
        this.navigationTabBar.setTitleSize(10);
        this.navigationTabBar.setIconSizeFraction(0.5f);
    }

    private void initDeviceInfo(View view) {
        RecyclerView rvItems = view.findViewById(R.id.frDeviceInfo_rvItems);
        rvItems.setHasFixedSize(true);
        if (rvItems.getLayoutManager() == null)
            rvItems.setLayoutManager(this.mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(this.deviceInfoAdapter);
    }

    private void initScreenInfo(View view) {
        RecyclerView rvItems = view.findViewById(R.id.frDeviceInfo_rvItems);
        rvItems.setHasFixedSize(true);
        if (rvItems.getLayoutManager() == null)
            rvItems.setLayoutManager(this.mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(this.screenInfoAdapter);
    }

    private void initMemoryInfo(View view) {
        RecyclerView rvItems = view.findViewById(R.id.frDeviceInfo_rvItems);
        rvItems.setHasFixedSize(true);
        if (rvItems.getLayoutManager() == null)
            rvItems.setLayoutManager(this.mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(this.memoryInfoAdapter);
    }

    private void initNetworkInfo(View view) {
        RecyclerView rvItems = view.findViewById(R.id.frDeviceInfo_rvItems);
        rvItems.setHasFixedSize(true);
        if (rvItems.getLayoutManager() == null)
            rvItems.setLayoutManager(this.mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(this.networkInfoAdapter);
    }

    private void initAppsInfo(View view) {
        RecyclerView rvItems = view.findViewById(R.id.frDeviceInfo_rvItems);
        rvItems.setHasFixedSize(true);
        if (rvItems.getLayoutManager() == null)
            rvItems.setLayoutManager(this.mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(this.userAppsAdapter);
    }

    private void initCpuInfo(View view) {
        RecyclerView rvItems = view.findViewById(R.id.frDeviceInfo_rvItems);
        rvItems.setHasFixedSize(true);
        if (rvItems.getLayoutManager() == null)
            rvItems.setLayoutManager(this.mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(this.cpuInfoAdapter);
    }

    private void initBatteryInfo(View view) {
        RecyclerView rvItems = view.findViewById(R.id.frDeviceInfo_rvItems);
        rvItems.setHasFixedSize(true);
        if (rvItems.getLayoutManager() == null)
            rvItems.setLayoutManager(this.mLayoutManager);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(this.batteryInfoAdapter);
    }


}
