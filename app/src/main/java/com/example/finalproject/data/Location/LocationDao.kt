package com.example.finalproject.data.Location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM location_table")
    suspend fun getAllLocations(): Flow<List<Location>>

    @Insert
    suspend fun insertLocation(location: Location)


}