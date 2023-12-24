package com.example.finalproject.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.example.finalproject.data.Course.Course
import com.example.finalproject.data.Location.Location

@Dao
interface RouteDao {
    @Insert
    suspend fun insertRoute(route: Route)

    @Transaction
    @Query("SELECT * FROM route_table")
    suspend fun getCRoutes(): List<Route>
}
