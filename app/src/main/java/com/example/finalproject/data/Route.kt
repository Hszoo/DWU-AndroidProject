package com.example.finalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "route_table")
data class Route(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val RouteName: String,
    val courseId: Long,
    val placeId: Long
)