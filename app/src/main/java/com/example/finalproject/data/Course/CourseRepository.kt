package com.example.finalproject.data.Course

import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {
    val allFoods : Flow<List<Course>> = courseDao.getAllCourses()

    suspend fun addCourse(course: Course) {
        courseDao.insertCourse(course)
    }

    suspend fun modifyCourse(courseId:Long, name: String, description: String) {
        courseDao.updateCourse(courseId, name, description)
    }

    suspend fun removeCourse(courseId: Long) {
        courseDao.deleteCourse(courseId)
    }

    suspend fun getAllCourses(country: String) : Flow<List<Course>> = courseDao.getAllCourses()
}