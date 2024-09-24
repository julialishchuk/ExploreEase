package com.example.exploreease.ui.personal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route
import com.example.exploreease.domain.manager.ActivityLogManager
import com.example.exploreease.domain.manager.FavoritesManager
import com.example.exploreease.domain.manager.RoutesManager
import com.example.exploreease.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonalPreferencesViewModel (
    private val favoritesManager: FavoritesManager,
    private val routesManager: RoutesManager,
    private val activityLogManager: ActivityLogManager
): ViewModel() {


    companion object {
        private final const val TAG = "PersonalPreferencesViewModel"
    }

    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _favoritePlaces = MutableLiveData<List<Place>>()
    val favoritesPlaces: LiveData<List<Place>> = _favoritePlaces

    private val _routes = MutableLiveData<List<Route>>()
    val routes: LiveData<List<Route>> = _routes

    private val _completedRoutes = MutableLiveData<List<Route>>()
    val completedRoutes: LiveData<List<Route>> = _completedRoutes

    init {
        getRoutes()
        getCompletedRoutes()
    }

    public fun getFavorites() {
        Log.d(TAG, "getFavorites ")
        viewModelScope.launch {
            val places = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                favoritesManager.getAllFavorites()
            }
            Log.d(TAG, "END !!!!!!!!! -  $places")
            _favoritePlaces.postValue(places)
        }
    }

    public fun addToFavorites(place: Place) {
        Log.d(TAG, "addToFavorites ")
        viewModelScope.launch {
             withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                favoritesManager.addToFavorite(place)
            }
        }
    }

    public fun getRoutes() {
        Log.d(TAG, "getRoutes ")
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                routesManager.getActiveRoutes()
            }
            Log.d(TAG, "END !!!!!!!!! -  $routes")
            _routes.postValue(routes)
        }
    }

    public fun removeRoute(route: Route) {
        Log.d(TAG, "removeRoute ")
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                routesManager.removeRoute(route)
                routesManager.getActiveRoutes()
            }
            Log.d(TAG, "END !!!!!!!!! -  $routes")
            _routes.postValue(routes)
        }
    }

    public fun completeRoute(route: Route) {
        Log.d(TAG, "completeRoute ")
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                routesManager.completeRoute(route.id, true)
                routesManager.getActiveRoutes()
            }
            Log.d(TAG, "END !!!!!!!!! -  $routes")
            _routes.postValue(routes)
        }
    }

    public fun removePlaceFromRoute(routeId: Int, placeId: String) {
        Log.d(TAG, "removePlaceFromRoute ")
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                routesManager.removeStopFromRoute(routeId,placeId)
                routesManager.getActiveRoutes()
            }
            Log.d(TAG, "END !!!!!!!!! -  $routes")
            _routes.postValue(routes)
        }
    }

    public fun changeRouteName(routeId: Int, newName: String) {
        Log.d(TAG, "changeRouteName ")
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                routesManager.changeRouteName(routeId, newName)
                routesManager.getActiveRoutes()
            }
            Log.d(TAG, "END !!!!!!!!! -  $routes")
            _routes.postValue(routes)
        }
    }

    public fun getCompletedRoutes() {
        Log.d(TAG, "getCompletedRoutes() ")
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                activityLogManager.getCompletedRoutes()
            }
            Log.d(TAG, "END !!!!!!!!! -  $routes")
            _completedRoutes.postValue(routes)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


    class Factory(
        private val favoritesManager: FavoritesManager,
        private val routesManager: RoutesManager,
        private val activityLogManager: ActivityLogManager
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PersonalPreferencesViewModel(
                favoritesManager,
                routesManager,
                activityLogManager
            ) as T
        }
    }

}


