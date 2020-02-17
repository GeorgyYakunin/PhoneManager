package com.fara.phonemanager.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fara.phonemanager.database.models.BatteryChangeReceiverStatusModel;

/*
 * This database stores the status of the battery change receiver if it is executed or not
 * */

@Dao
public interface BatteryChangeReceiverStatusDao {
    @Insert
    public void insert(BatteryChangeReceiverStatusModel... items);

    @Update
    public void update(BatteryChangeReceiverStatusModel... items);

    @Delete
    public void delete(BatteryChangeReceiverStatusModel... item);

    @Query("DELETE FROM battery_change_receiver_status")
    public void nukeTable();

    @Query("SELECT battery_change_receiver_registered FROM battery_change_receiver_status ORDER BY bcrsid DESC LIMIT 1")
    public boolean isBatteryChangeReceiverRegistered();
}