package com.example.exploreease.domain.manager

import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLng

class PlacesManager(private val placesRepository: PlacesRepository) {

    fun getPlaceByCoordinates(latLng: LatLng): Place? {
        return placesRepository.getPlaceByCoordinates(latLng)
    }

    fun getPlaceById(placeId: String): Place? {
        return placesRepository.getPlaceById(placeId)
    }

    fun searchPlaceByName(name: String): Place? {
        return placesRepository.searchPlaceByName(name)
    }


}