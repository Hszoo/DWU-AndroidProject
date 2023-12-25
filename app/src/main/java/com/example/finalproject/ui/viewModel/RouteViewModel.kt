//package com.example.finalproject.ui.viewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.asLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.finalproject.data.Route
//import com.example.finalproject.data.RouteRepository
//import kotlinx.coroutines.launch
//
//class RouteViewModel (val repository : RouteRepository) : ViewModel() {
//    var allRoutes: LiveData<List<Route>> = repository.allRoutes.asLiveData()
//
//    fun addRoute(route: Route) = viewModelScope.launch {
//        repository.addRoute(route)
//    }
//
//    fun modifyRoute(routeName: String, routeId: Long) = viewModelScope.launch {
//        repository.modifyRoute(routeName, routeId)
//    }
//
//    fun removeRoute(routeName: String) = viewModelScope.launch {
//        repository.removeRoute(routeName)
//    }
//}
//
//class RouteViewModelFactory(private val repository: RouteRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(RouteViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return RouteViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}