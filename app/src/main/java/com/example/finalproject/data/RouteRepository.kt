package com.example.finalproject.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class RouteRepository(private val routeDao: RouteDao) {
    val allRoutes : Flow<List<Route>> = routeDao.getAllRoutes()
    suspend fun addRoute(route: Route) {
        routeDao.insertRoute(route)
    }

    suspend fun modifyRoute(routeName: String, routeId: Long) {
        routeDao.updateRoute(routeName, routeId)
    }

    suspend fun removeRoute(routeName: String) {
        routeDao.deleteRoute(routeName)
    }

    suspend fun getRouteByRouteName(routeName: String) : List<Route> = routeDao.getRouteByRouteName(routeName)
}