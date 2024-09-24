package com.example.exploreease.domain.repository

import com.example.exploreease.domain.entity.Place
import com.google.android.gms.maps.model.LatLng

interface PlacesRepository {
    fun getPlaceByCoordinates(latLng: LatLng): Place?

    fun getPlaceById(placeId: String): Place?

    fun searchPlaceByName(name: String): Place?

}