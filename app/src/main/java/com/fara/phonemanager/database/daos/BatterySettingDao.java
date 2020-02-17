package com.fara.phonemanager.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fara.phonemanager.database.models.BatterySettingModel;

/*
 * This database holds the status of battery screen options whether or not they are enabled.
 * */

@Dao
public interface BatterySettingDao {
    @Insert
    public void insert(BatterySettingModel... items);

    @Update
    public void update(BatterySettingModel... items);

    @Delete
    public void delete(BatterySettingModel... item);

    @Query("DELETE FROM battery_setting")
    public void nukeTable();

    @Query("SELECT notification FROM battery_setting")
    public int getNotification();

    @Query("SELECT brightness FROM battery_setting")
    public int getBrightness();

    @Query("SELECT COUNT(*) FROM battery_setting")
    public int getSize();
}