package com.example.finalproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Junction
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.example.finalproject.data.Course.Course
import com.example.finalproject.data.Location.Location
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Insert
    suspend fun insertRoute(route: Route)

    @Query("SELECT * FROM route_table WHERE routeName = :routeName")
    suspend fun getRouteByRouteName(routeName: String) : List<Route>

    // 입력받은 route pk 로 수정
    @Query("UPDATE route_table SET routeName = :routeName WHERE id = :id")
    suspend fun updateRoute(routeName : String, id: Long)

    @Query("DELETE FROM route_table WHERE routeName = :routeName")
    suspend fun deleteRoute(routeName : String)

    @Query("SELECT * FROM route_table")
    fun getAllRoutes(): Flow<List<Route>>
}
