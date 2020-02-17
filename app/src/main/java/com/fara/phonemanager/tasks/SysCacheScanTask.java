package com.fara.phonemanager.tasks;

import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.os.storage.StorageManager;

import com.fara.phonemanager.AppController;
import com.fara.phonemanager.R;
import com.fara.phonemanager.callbacks.IScanCallback;
import com.fara.phonemanager.info.JunkInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SysCacheScanTask extends AsyncTask<Void, Void, Void> {
    private IScanCallback mCallback;

    private int mScanCount;
    private int mTotalCount;
    private ArrayList<JunkInfo> mSysCaches;
    private HashMap<String, String> mAppNames;
    private long mTotalSize = 0L;

    public SysCacheScanTask(IScanCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.mCallback.onBegin();
        PackageManager pm = AppController.getContext().getPackageManager();
        List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        IPackageStatsObserver.Stub observer = new PackageStatsObserver();

        this.mScanCount = 0;
        this.mTotalCount = installedPackages.size();
        this.mSysCaches = new ArrayList<>();
        this.mAppNames = new HashMap<>();

        for (int i = 0; i < mTotalCount; i++) {
            ApplicationInfo info = installedPackages.get(i);
            this.mAppNames.put(info.packageName, pm.getApplicationLabel(info).toString());
            getPackageInfo(info.packageName, observer);
        }

        return null;
    }

    public void getPackageInfo(String packageName, IPackageStatsObserver.Stub observer) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                PackageManager pm = AppController.getContext().getPackageManager();
                Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

                getPackageSizeInfo.invoke(pm, packageName, observer);
            } else {
                final StorageStatsManager storageStatsManager = (StorageStatsManager) AppController.getContext().getSystemService(Context.STORAGE_STATS_SERVICE);
                final StorageManager storageManager = (StorageManager) AppController.getContext().getSystemService(Context.STORAGE_SERVICE);
                try {

                    ApplicationInfo ai = AppController.getContext().getPackageManager().getApplicationInfo(packageName, 0);
                    StorageStats storageStats = storageStatsManager.queryStatsForUid(ai.storageUuid, AppController.getContext().getApplicationInfo().uid);
                    long cacheSize = storageStats.getCacheBytes();
                    long dataSize = storageStats.getDataBytes();
                    long apkSize = storageStats.getAppBytes();

                    PackageStats packageStats = new PackageStats(packageName);
                    packageStats.cacheSize = cacheSize;
                    packageStats.dataSize = dataSize;
                    packageStats.codeSize = apkSize;
                    observer.onGetStatsCompleted(packageStats, true);
//                    long size += info.cacheSize;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private class PackageStatsObserver extends IPackageStatsObserver.Stub {
        @Override
        public void onGetStatsCompleted(PackageStats packageStats, boolean succeeded) throws RemoteException {
            mScanCount++;
            if (packageStats == null || !succeeded) {
            } else {
                JunkInfo info = new JunkInfo();
                info.mPackageName = packageStats.packageName;
                info.name = mAppNames.get(info.mPackageName);
                info.mSize = packageStats.cacheSize + packageStats.externalCacheSize;
                if (info.mSize > 0) {
                    mSysCaches.add(info);
                    mTotalSize += info.mSize;
                }
                mCallback.onProgress(info);
            }

            if (mScanCount == mTotalCount) {
                JunkInfo info = new JunkInfo();
                info.name = AppController.getContext().getResources().getString(R.string.junk_clean_system_cache);
                info.mSize = mTotalSize;
                Collections.sort(mSysCaches);
                Collections.reverse(mSysCaches);
                info.mChildren = mSysCaches;
                info.mIsVisible = true;
                info.mIsChild = false;

                ArrayList<JunkInfo> list = new ArrayList<>();
                list.add(info);
                mCallback.onFinish(list);
            }
        }
    }
}