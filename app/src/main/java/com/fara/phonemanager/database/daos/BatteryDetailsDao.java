package com.fara.phonemanager.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fara.phonemanager.database.models.BatteryDetailsModel;

/*
 * In this database, the voltage - level - temperature of the battery is saved.
 * */

@Dao
public interface BatteryDetailsDao {
    @Insert
    public void insert(BatteryDetailsModel... items);

    @Update
    public void update(BatteryDetailsModel... items);

    @Delete
    public void delete(BatteryDetailsModel... item);

    @Query("DELETE FROM battery_details")
    public void nukeTable();

    @Query("SELECT scale FROM battery_details")
    public int getScale();

    @Query("SELECT level FROM battery_details")
    public int getLevel();

    @Query("SELECT voltage FROM battery_details")
    public int getVoltage();

    @Query("SELECT temperature FROM battery_details")
    public int getTemperature();

    @Query("SELECT COUNT(*) FROM battery_details")
    public int getSize();
}