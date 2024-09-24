package com.example.exploreease.data.retrofit.service

import com.example.exploreease.data.retrofit.model.GeocodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoApiService {
    @GET("maps/api/geocode/json")
    fun getPlaceIdByCoordinates(
        @Query("latlng") latlng: String,
        @Query("key") apiKey: String
    ): Call<GeocodeResponse>
}

