package com.fara.phonemanager.tasks;

import android.os.AsyncTask;
import android.os.Environment;

import com.fara.phonemanager.callbacks.IScanCallback;
import com.fara.phonemanager.info.JunkInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class OverallScanTask extends AsyncTask<Void, Void, Void> {
    private IScanCallback mCallback;
    private final int SCAN_LEVEL = 4;
    private JunkInfo mApkInfo;
    private JunkInfo mLogInfo;
    private JunkInfo mTmpInfo;

    public OverallScanTask(IScanCallback callback) {
        this.mCallback = callback;
        this.mApkInfo = new JunkInfo();
        this.mLogInfo = new JunkInfo();
        this.mTmpInfo = new JunkInfo();
    }

    private void travelPath(File root, int level) {
        if (root == null || !root.exists() || level > SCAN_LEVEL) {
            return;
        }

        File[] lists = root.listFiles();
        if (lists != null) {
            for (File file : lists) {
                if (file.isFile()) {
                    String name = file.getName();
                    JunkInfo info = null;
                    if (name.endsWith(".apk")) {
                        info = new JunkInfo();
                        info.mSize = file.length();
                        // TODO 解析出 apk 文件详细内容
                        info.name = name;
                        info.mPath = file.getAbsolutePath();
                        info.mIsChild = false;
                        info.mIsVisible = true;
                        this.mApkInfo.mChildren.add(info);
                        this.mApkInfo.mSize += info.mSize;
                    } else if (name.endsWith(".log")) {
                        info = new JunkInfo();
                        info.mSize = file.length();
                        info.name = name;
                        info.mPath = file.getAbsolutePath();
                        info.mIsChild = false;
                        info.mIsVisible = true;
                        this.mLogInfo.mChildren.add(info);
                        this.mLogInfo.mSize += info.mSize;
                    } else if (name.endsWith(".tmp") || name.endsWith(".temp")) {
                        info = new JunkInfo();
                        info.mSize = file.length();
                        info.name = name;
                        info.mPath = file.getAbsolutePath();
                        info.mIsChild = false;
                        info.mIsVisible = true;
                        this.mTmpInfo.mChildren.add(info);
                        this.mTmpInfo.mSize += info.mSize;
                    }

                    if (info != null) {
                        this.mCallback.onProgress(info);
                    }
                } else {
                    if (level < SCAN_LEVEL) {
                        travelPath(file, level + 1);
                    }
                }
            }
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.mCallback.onBegin();

        File externalDir = Environment.getExternalStorageDirectory();
        if (externalDir != null) {
            travelPath(externalDir, 0);
        }

        ArrayList<JunkInfo> list = new ArrayList<>();

        if (this.mApkInfo.mSize > 0L) {
            Collections.sort(this.mApkInfo.mChildren);
            Collections.reverse(this.mApkInfo.mChildren);
            list.add(this.mApkInfo);
        }

        if (this.mLogInfo.mSize > 0L) {
            Collections.sort(this.mLogInfo.mChildren);
            Collections.reverse(this.mLogInfo.mChildren);
            list.add(this.mLogInfo);
        }

        if (this.mTmpInfo.mSize > 0L) {
            Collections.sort(this.mTmpInfo.mChildren);
            Collections.reverse(this.mTmpInfo.mChildren);
            list.add(this.mTmpInfo);
        }

        this.mCallback.onFinish(list);

        return null;
    }
}