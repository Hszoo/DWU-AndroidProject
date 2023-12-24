package com.example.finalproject.data.Course

import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {
    val allFoods : Flow<List<Course>> = courseDao.getAllCourses()

    suspend fun addCourse(course: Course) {
        courseDao.insertCourse(course)
    }

    suspend fun modifyCourse(name: String, description: String) {
        courseDao.updateCourse(name, description)
    }

    suspend fun removeFood(courseName: String) {
        courseDao.deleteCourse(courseName)
    }

    suspend fun getAllCourses(country: String) : Flow<List<Course>> = courseDao.getAllCourses()
}