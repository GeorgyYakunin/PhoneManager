package com.fara.phonemanager.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "admob")
public class AdMobModel {
    @ColumnInfo(name = "amid")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "junk_file")
    private int admobJunkFile;

    @ColumnInfo(name = "save_browser")
    private int admobSaveBrowser;

    @ColumnInfo(name = "scan")
    private int admobScan;

    @ColumnInfo(name = "update_phone_manager")
    private int admobUpdatePhoneManager;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdmobJunkFile() {
        return admobJunkFile;
    }

    public void setAdmobJunkFile(int admobJunkFile) {
        this.admobJunkFile = admobJunkFile;
    }

    public int getAdmobSaveBrowser() {
        return admobSaveBrowser;
    }

    public void setAdmobSaveBrowser(int admobSaveBrowser) {
        this.admobSaveBrowser = admobSaveBrowser;
    }

    public int getAdmobScan() {
        return admobScan;
    }

    public void setAdmobScan(int admobScan) {
        this.admobScan = admobScan;
    }

    public int getAdmobUpdatePhoneManager() {
        return admobUpdatePhoneManager;
    }

    public void setAdmobUpdatePhoneManager(int admobUpdatePhoneManager) {
        this.admobUpdatePhoneManager = admobUpdatePhoneManager;
    }
}