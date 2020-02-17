package com.fara.phonemanager.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.fara.phonemanager.database.daos.BatteryChangeReceiverStatusDao;
import com.fara.phonemanager.database.daos.BatteryDetailsDao;
import com.fara.phonemanager.database.daos.BatterySettingDao;
import com.fara.phonemanager.database.daos.AdMobDao;
import com.fara.phonemanager.database.models.BatteryChangeReceiverStatusModel;
import com.fara.phonemanager.database.models.BatteryDetailsModel;
import com.fara.phonemanager.database.models.BatterySettingModel;
import com.fara.phonemanager.database.models.AdMobModel;

@Database(entities = {AdMobModel.class, BatteryDetailsModel.class, BatterySettingModel.class, BatteryChangeReceiverStatusModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AdMobDao getTapSellDAO();
    public abstract BatteryDetailsDao getBatteryDetailsDao();
    public abstract BatterySettingDao getBatterySettingDao();
    public abstract BatteryChangeReceiverStatusDao getBatteryChangeReceiverStatusDao();
}