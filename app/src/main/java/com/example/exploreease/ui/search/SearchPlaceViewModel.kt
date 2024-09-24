package com.example.exploreease.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route
import com.example.exploreease.domain.manager.FavoritesManager
import com.example.exploreease.domain.manager.PlacesManager
import com.example.exploreease.domain.manager.RoutesManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPlaceViewModel(
    private val placesManager: PlacesManager,
    private val favoritesManager: FavoritesManager,
    private val routesManager: RoutesManager
) : ViewModel() {

    companion object {
        private final const val TAG = "SearchPlaceViewModel"
    }


    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _selectedPlaceLiveData = MutableLiveData<Place?>()
    val selectedPlaceLiveData: LiveData<Place?> = _selectedPlaceLiveData

    private val _routesLiveData = MutableLiveData<List<Route>>()
    val routesLiveData: LiveData<List<Route>> = _routesLiveData

    private val _isSelectedPlaceFavorite = MutableLiveData<Boolean>()
    val isSelectedPlaceFavorite: LiveData<Boolean?> = _isSelectedPlaceFavorite

    val searchPlaceLiveData = MutableLiveData<Place?>()

    var isPlaceHandled = false

    init {
        updateRoutesList()
        observeInit()


    }

    private fun observeInit() {
        _selectedPlaceLiveData.observeForever {
            viewModelScope.launch {
                val isFav = withContext(Dispatchers.IO) {
                    favoritesManager.isFavorite(it)
                }
                _isSelectedPlaceFavorite.postValue(isFav)
            }
        }
    }




    public fun handleMapClick(latLng: LatLng) {
        Log.d(TAG, "handleMapClick() ")
        viewModelScope.launch {
            val place = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                placesManager.getPlaceByCoordinates(latLng)
            }
            Log.d(TAG, "END !!!!!!!!! -  $place")
            isPlaceHandled = false
            _selectedPlaceLiveData.postValue(place)
        }
    }

    public fun handlePointOfInterestClick(placeId: String) {
        Log.d(TAG, "handlePointOfInterestClick ")
        viewModelScope.launch {
            val place = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                placesManager.getPlaceById(placeId)
            }
            Log.d(TAG, "END !!!!!!!!! -  $place")
            isPlaceHandled = false
            _selectedPlaceLiveData.postValue(place)
        }
    }

    public fun handlePlaceSearch(name: String) {
        Log.d(TAG, "handlePlaceSearch ")
        viewModelScope.launch {
            val place = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                placesManager.searchPlaceByName(name)
            }
            Log.d(TAG, "END !!!!!!!!! -  $place")
            searchPlaceLiveData.postValue(place)
        }
    }

    public fun handleCreateRoute(name :String) {
        Log.d(TAG, "handleCreateRoute ")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                //replace this
                routesManager.createRoute(
                    Route(
                        0,
                        name,
                        0.0,
                        ArrayList<Place>(),
                        Place.EMPTY_PLACE,
                        false
                    )
                )
            }
            updateRoutesList()
        }
    }


    public fun handleAddStopToRoute(place: Place, routedId : Int) {
        Log.d(TAG, "handleAddStopToRoute ")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                routesManager.addStopToRoute(place, routedId)
            }
        }
    }

    public fun updateRoutesList() {
        Log.d(TAG, "updateRoutesList ")
        viewModelScope.launch {
            val routes = withContext(Dispatchers.IO) {
                Log.d(TAG, "start coroutine")
                routesManager.getActiveRoutes()
            }
            Log.d(TAG, "END !!!!!!!!! -  $routes")
            _routesLiveData.postValue(routes)
        }
    }

    public fun handleOnFavoriteClick(place: Place) {
        Log.d(TAG, "handleOnFavoriteClick ")
        viewModelScope.launch {
            val isFav = withContext(Dispatchers.IO) {
                if (!favoritesManager.isFavorite(place)) {
                    favoritesManager.addToFavorite(place)
                } else favoritesManager.removeFromFavorite(place)

                favoritesManager.isFavorite(place)
            }
            _isSelectedPlaceFavorite.postValue(isFav)
        }
    }





    class Factory(
        private val placesManager: PlacesManager,
        private val favoritesManager: FavoritesManager,
        private val routesManager: RoutesManager
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchPlaceViewModel(
                placesManager,
                favoritesManager,
                routesManager
            ) as T
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}