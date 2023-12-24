package com.example.finalproject.data.Course

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_table")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val courseId: Long = 0,
    val courseName: String,
    val description: String?,

    )