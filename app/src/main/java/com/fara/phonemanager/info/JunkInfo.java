package com.fara.phonemanager.info;

import com.fara.phonemanager.AppController;
import com.fara.phonemanager.R;

import java.util.ArrayList;

public class JunkInfo implements Comparable<JunkInfo> {
    public String name;
    public long mSize;
    public String mPackageName;
    public String mPath;
    public ArrayList<JunkInfo> mChildren = new ArrayList<>();
    public boolean mIsVisible = false;
    public boolean mIsChild = true;

    @Override
    public int compareTo(JunkInfo another) {
        String top = AppController.getContext().getResources().getString(R.string.junk_clean_system_cache);

        if (this.name != null && this.name.equals(top)) {
            return 1;
        }

        if (another.name != null && another.name.equals(top)) {
            return -1;
        }

        if (this.mSize > another.mSize) {
            return 1;
        } else if (this.mSize < another.mSize) {
            return -1;
        } else {
            return 0;
        }
    }
}