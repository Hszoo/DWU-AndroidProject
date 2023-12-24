package com.example.finalproject.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalproject.data.Course.Course
import com.example.finalproject.data.Course.CourseRepository
import kotlinx.coroutines.launch

class CourseViewModel (val repository : CourseRepository) : ViewModel() {
    var allCourses: LiveData<List<Course>> = repository.allFoods.asLiveData()

    fun addCourse(course: Course) = viewModelScope.launch {
        repository.addCourse(course)
    }

    fun modifyCourse(courseId:Long, courseName: String, country: String) = viewModelScope.launch {
        repository.modifyCourse(courseId, courseName, country)
    }

    fun removeCourse(courseId: Long) = viewModelScope.launch {
        repository.removeCourse(courseId)
    }
}

// FoodViewModelFactory 를 별개의 클래스로 작성하는 것도 가능
class CourseViewModelFactory(private val repository: CourseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}