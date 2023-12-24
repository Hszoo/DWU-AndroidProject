package com.example.finalproject.data.Course

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert
    suspend fun insertCourse(vararg course: Course)

    @Query("UPDATE course_table SET courseName = :name, description =:description WHERE courseId = :courseId")
    suspend fun updateCourse(courseId: Long, name : String, description: String)

    @Query("DELETE FROM course_table WHERE courseId = :courseId")
    suspend fun deleteCourse(courseId : Long)

    @Query("SELECT * FROM course_table")
    fun getAllCourses(): Flow<List<Course>>

}
