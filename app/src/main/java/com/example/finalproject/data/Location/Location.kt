package com.example.finalproject.data.Location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val locationId: Long = 0,
    val name: String,
    val description: String?,
    val latitude: Double,
    val longitude: Double
)
