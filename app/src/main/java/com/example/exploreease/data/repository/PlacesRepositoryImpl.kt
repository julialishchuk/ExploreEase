package com.example.exploreease.data.repository

import android.os.RemoteException
import android.util.Log
import com.example.exploreease.data.parser.ResponseParser
import com.example.exploreease.data.retrofit.MapsApiInstance
import com.example.exploreease.data.retrofit.service.GeoApiService
import com.example.exploreease.data.retrofit.service.PlacesApiService
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.repository.PlacesRepository
import com.google.android.gms.maps.model.LatLng

class PlacesRepositoryImpl(
    private val geoApiService: GeoApiService,
    private val placesApiService: PlacesApiService,
) : PlacesRepository {

    companion object {
        private final const val TAG = "PlacesRepositoryImpl 33333333333"
    }

    override fun getPlaceByCoordinates(latLng: LatLng): Place? {
        Log.d(TAG, "getPlaceByCoordinates()")
        val foundPlaceId = getPlaceIdByCoordinates(latLng) ?: return null
        return getPlaceById(foundPlaceId)
    }

    override fun getPlaceById(placeId: String): Place? {
        Log.d(TAG, "getPlaceById() ")
        var place: Place? = null

        try {
            val response =
                placesApiService.getPlaceDetails(placeId, MapsApiInstance.API_KEY).execute()
            if (!response.isSuccessful || response.body() == null) {
                Log.e(TAG, "API call response failed")
            }
            Log.d(TAG, " getPlaceDetails - OK onResponse ")
            place = ResponseParser.parsePlaceDetailsResponse(response.body()!!)
            Log.d(TAG, "API:  Found place by id = " + response.body()!!.result)
            Log.d(TAG, "Parsed place = $place")
        } catch (e: Exception) {
            Log.e(TAG, "API call exception:$e")
        }

        return place
    }

    override fun searchPlaceByName(name: String): Place? {
        Log.d(TAG, "searchPlaceByName() ")
        var place: Place? = null

        try {
            val response =
                placesApiService.searchPlacesByName(name, MapsApiInstance.API_KEY).execute()
            if (!response.isSuccessful || response.body() == null) {
                Log.e(TAG, "API call response failed")
            }
            Log.d(TAG, " searchPlaceByName - OK onResponse ")
            place = ResponseParser.parsePlaceSearchResponse(response.body()!!)
            Log.d(TAG, "Parsed place = $place")
        } catch (e: RemoteException) {
            Log.e(TAG, "API call exception:$e")
        }

        return place
    }



    private fun getPlaceIdByCoordinates(latLng: LatLng): String? {
        Log.d(TAG, "getPlaceIdByCoordinates()")
        val latLngStr = "${latLng.latitude},${latLng.longitude}"
        var placeId: String? = null

        try {
            val response =
                geoApiService.getPlaceIdByCoordinates(latLngStr, MapsApiInstance.API_KEY).execute()
            if (!response.isSuccessful || response.body() == null || response.body()?.results?.isNullOrEmpty() == true) {
                Log.e(TAG, "API call response failed")
            }
            Log.d(TAG, " getPlaceIdByCoordinates - OK onResponse ")
            placeId = ResponseParser.parsePlaceIdFromGeocodeResponse(response.body()!!)
            Log.d(TAG, "API:  Found place by id = " + (response.body()!!.results?.get(0) ?: ""))
            Log.d(TAG, "Parsed place = $placeId")

        } catch (e: Exception) {
            Log.e(TAG, "API call exception:$e")
        }
        return placeId
    }





}




