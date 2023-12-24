package com.example.finalproject

import android.app.Application
import com.example.finalproject.data.Location.LocationDatabase
import com.example.finalproject.data.Location.LocationRepository

class LocationApplication: Application() {
    val database by lazy {
        LocationDatabase.getDatabase(this)
    }

    val repository by lazy {
        LocationRepository(database.locationDao())
    }
}

