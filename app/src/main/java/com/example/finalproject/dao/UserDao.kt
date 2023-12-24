package com.example.finalproject.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finalproject.data.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Insert
    fun insertUser(vararg user:User)

    @Update
    fun updateUser(user:User)

    @Delete
    fun deleteUser(user:User)

    // 중복검사를 위해 사용
    @Query("SELECT id FROM user")
    fun getAllUsersId(): Flow<List<String>>

}