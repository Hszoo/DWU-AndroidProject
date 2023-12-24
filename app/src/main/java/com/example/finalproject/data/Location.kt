package com.example.finalproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Location(
    @PrimaryKey val locationId: Long,

    val courseId: String // course id 참조
)