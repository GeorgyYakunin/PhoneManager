package com.fara.phonemanager.utils.junkclean;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/*
* For ActivityBasicGarbageClean class
* */

public class DiskStat {

    private long mExternalBlockSize;
    private long mExternalBlockCount;
    private long mExternalAvailableBlocks;

    private long mInternalBlockSize;
    private long mInternalBlockCount;
    private long mInternalAvailableBlocks;

    public DiskStat() {
        calculateInternalSpace();
        calculateExternalSpace();
    }

    // Returns the total amount of memory space to the program
    public long getTotalSpace() {
        return this.mInternalBlockSize * this.mInternalBlockCount + this.mExternalBlockSize * this.mExternalBlockCount;
    }

    // Returns the amount of space occupied by the program
    public long getUsedSpace() {
        return this.mInternalBlockSize * (this.mInternalBlockCount - this.mInternalAvailableBlocks) + this.mExternalBlockSize * (this.mExternalBlockCount - this.mExternalAvailableBlocks);
    }

    // Returns the amount of space that can be used by the application
    public long getUsableSpace() {
        return this.mInternalBlockSize * this.mInternalAvailableBlocks + this.mExternalBlockSize * this.mExternalAvailableBlocks;
    }

    // Calculate SD card space and internal memory of the phone
    private void calculateExternalSpace() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                this.mExternalBlockSize = sf.getBlockSizeLong();
                this.mExternalBlockCount = sf.getBlockCountLong();
                this.mExternalAvailableBlocks = sf.getAvailableBlocksLong();
            } else {
                this.mExternalBlockSize = sf.getBlockSize();
                this.mExternalBlockCount = sf.getBlockCount();
                this.mExternalAvailableBlocks = sf.getAvailableBlocks();
            }
        }
    }

    private void calculateInternalSpace() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            this.mInternalBlockSize = sf.getBlockSizeLong();
            this.mInternalBlockCount = sf.getBlockCountLong();
            this.mInternalAvailableBlocks = sf.getAvailableBlocksLong();
        } else {
            this.mInternalBlockSize = sf.getBlockSize();
            this.mInternalBlockCount = sf.getBlockCount();
            this.mInternalAvailableBlocks = sf.getAvailableBlocks();
        }
    }
}
