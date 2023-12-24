package com.example.finalproject

import android.app.Application
import com.example.finalproject.data.Course.CourseDatabase
import com.example.finalproject.data.Course.CourseRepository
import com.example.finalproject.data.RouteDatabase
import com.example.finalproject.data.RouteRepository

class CourseApplication: Application() {
    val database by lazy {
        CourseDatabase.getDatabase(this)
    }

    val repository by lazy {
        CourseRepository(database.courseDao())
    }
}