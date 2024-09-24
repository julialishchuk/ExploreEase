package com.example.exploreease.domain.manager

import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.repository.FavoritesRepository
import com.google.android.gms.maps.model.LatLng

class FavoritesManager(private val favoritesRepo: FavoritesRepository) {

    fun getAllFavorites(): List<Place> {
        return favoritesRepo.getAllFavorites()
    }

    fun isFavorite(place: Place?): Boolean {
        if (place != null) {
            return favoritesRepo.isFavorite(place)
        }
        return false
    }

    fun addToFavorite(place: Place) {
        return favoritesRepo.addToFavorite(place)
    }

    fun removeFromFavorite(place: Place) {
        favoritesRepo.removeFromFavorite(place)
    }

}