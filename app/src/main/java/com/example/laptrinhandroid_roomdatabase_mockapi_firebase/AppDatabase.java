package com.example.laptrinhandroid_roomdatabase_mockapi_firebase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Wood.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WoodDAO woodDAO();
}
