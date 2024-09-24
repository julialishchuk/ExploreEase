package com.example.exploreease.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.manager.ActivityLogManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val activityLogManager: ActivityLogManager) : ViewModel() {

    companion object {
        private final const val TAG = "HomeViewModel"
    }

    private val viewModelScope = CoroutineScope(Dispatchers.Main)


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    init {
    }

    public fun getNearbyPlaces() : List<Place>{
        val list = ArrayList<Place>()
        list.add(Place("1", "Lviv High Castle", null, "Zamkova Street, Lviv, Ukraine",""))
        list.add(Place("1", "Lviv National Opera", null, "Svobody Avenue, 28, Lviv, Ukraine",""))
        list.add(Place("1", "Lviv City Hall", null, "Rynok Square, 1, Lviv, Ukraine",""))
        list.add(Place("1", "Armenian Cathedral of Lviv", null, "Virmenska Street, 7/13, Lviv, Ukraine",""))
        list.add(Place("1", "Lviv Historical Museum ", null, "Rynok Square, 6, Lviv, Ukraine",""))
        list.add(Place("1", "Potocki Palace", null, "Kopernyka Street, 15, Lviv, Ukraine",""))
        list.add(Place("1", "Lychakiv Cemetery", null, "Mechnykova Street, 33, Lviv, Ukraine",""))
        list.add(Place("1", "Dominican Church ", null, "Museum Square, 1, Lviv, Ukraine",""))
        list.add(Place("1", "St. George's Cathedral", null, "St. George Square, 5, Lviv, Ukraine",""))
        return list
    }


    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }


    class Factory(
        private val activityLogManager: ActivityLogManager
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(
                activityLogManager
            ) as T
        }
    }
}