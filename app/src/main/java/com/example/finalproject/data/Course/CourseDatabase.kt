package com.example.finalproject.data.Course

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Singleton 패턴 적용
@Database(entities = [Course::class], version=1)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao() : CourseDao
    companion object {
        @Volatile       // Main memory 에 저장한 값 사용
        private var INSTANCE : CourseDatabase? = null

        // INSTANCE 가 null 이 아니면 반환, null 이면 생성하여 반환
        fun getDatabase(context: Context) : CourseDatabase {
            return INSTANCE ?: synchronized(this) {     // 단일 스레드만 접근
                val instance = Room.databaseBuilder(context.applicationContext,
                    CourseDatabase::class.java, "course_db").build()
                INSTANCE = instance
                instance
            }
        }
    }

}


