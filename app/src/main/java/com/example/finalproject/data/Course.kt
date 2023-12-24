package com.example.finalproject.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Course(
    @PrimaryKey val courseId: Long,

    val courseTitle: String,
    val courseContent : String?,
    val courseHonorId: String // 저장한 사용자 아이디
)