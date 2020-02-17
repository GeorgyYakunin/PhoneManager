package com.fara.phonemanager.callbacks;

import com.fara.phonemanager.info.JunkInfo;

import java.util.ArrayList;

public interface IScanCallback {
    void onBegin();
    void onProgress(JunkInfo info);
    void onFinish(ArrayList<JunkInfo> children);
}