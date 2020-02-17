package com.fara.phonemanager.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fara.phonemanager.database.models.AdMobModel;

/*
 * In this database the advertising scenario shown in the option has been changed
 * */

@Dao
public interface AdMobDao {
    @Insert
    public void insert(AdMobModel... items);

    @Update
    public void update(AdMobModel... items);

    @Delete
    public void delete(AdMobModel... item);

    @Query("DELETE FROM admob")
    public void nukeTable();

    @Query("SELECT junk_file FROM admob")
    public int getJunkFile();

    @Query("SELECT save_browser FROM admob")
    public int getSaveBrowser();

    @Query("SELECT scan FROM admob")
    public int getScan();

    @Query("SELECT update_phone_manager FROM admob")
    public int getUpdatePhoneManager();
}
