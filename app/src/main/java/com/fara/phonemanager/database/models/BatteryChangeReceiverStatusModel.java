package com.fara.phonemanager.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "battery_change_receiver_status")
public class BatteryChangeReceiverStatusModel {
    @ColumnInfo(name = "bcrsid")
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "battery_change_receiver_registered")
    private boolean batteryChangeReceiverRegistered;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBatteryChangeReceiverRegistered() {
        return this.batteryChangeReceiverRegistered;
    }

    public void setBatteryChangeReceiverRegistered(boolean batteryChangeReceiverRegistered) {
        this.batteryChangeReceiverRegistered = batteryChangeReceiverRegistered;
    }
}