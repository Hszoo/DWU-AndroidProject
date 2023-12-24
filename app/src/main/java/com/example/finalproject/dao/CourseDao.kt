package com.example.finalproject.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.finalproject.data.Course

@Dao
interface CourseDao {
    @Insert
    fun insertCourse(vararg course: Course)

    @Update
    fun updateCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Query("SELECT courseId FROM course")
    fun getAllcourse(): List<Course>
}