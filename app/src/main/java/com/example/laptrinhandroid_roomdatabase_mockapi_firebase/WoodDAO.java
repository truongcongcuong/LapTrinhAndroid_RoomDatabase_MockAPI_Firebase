package com.example.laptrinhandroid_roomdatabase_mockapi_firebase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WoodDAO {
    @Query("select * from Wood")
    List<Wood> findALL();

    @Insert
    void addNew(Wood... woods);

    @Update
    void update(Wood... woods);

    @Delete
    void delete(Wood wood);
}
