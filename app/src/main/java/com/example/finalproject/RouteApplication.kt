//package com.example.finalproject
//
//import android.app.Application
//import com.example.finalproject.data.RouteDatabase
//import com.example.finalproject.data.RouteRepository
//
//class RouteApplication: Application() {
//    val database by lazy {
//        RouteDatabase.getDatabase(this)
//    }
//
//    val repository by lazy {
//        RouteRepository(database.routeDao())
//    }
//}