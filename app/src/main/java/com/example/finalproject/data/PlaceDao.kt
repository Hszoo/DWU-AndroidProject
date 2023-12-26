package com.example.finalproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaceDao {
    @Insert
    fun insertPlace(vararg place:Item)

    @Update
    fun updatePlace(place:Item)

    @Delete
    fun deletePlace(place:Item)

    @Query("SELECT * FROM place_table WHERE _id=:id")
    fun getPlaceById(id:Int) : Item

    @Query("SELECT * FROM place_table")
    fun getAllPlaces() : List<Item>
}