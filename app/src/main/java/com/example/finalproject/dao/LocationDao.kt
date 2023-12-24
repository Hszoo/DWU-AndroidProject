package com.example.finalproject.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finalproject.data.Location

@Dao
interface LocationDao {
    @Insert
    fun insertLocation(vararg location:Location)

    @Update
    fun updateLocation(location:Location)

    @Delete
    fun deleteLocation(location:Location)

    @Query("SELECT locId FROM location")
    fun getAllLocation(): Array<Location>

}