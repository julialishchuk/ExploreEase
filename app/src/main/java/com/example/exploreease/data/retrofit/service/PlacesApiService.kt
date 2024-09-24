package com.example.exploreease.data.retrofit.service

import com.example.exploreease.data.retrofit.model.PlaceDetailsResponse
import com.example.exploreease.data.retrofit.model.PlacesSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApiService {
    @GET("maps/api/place/details/json")
    fun getPlaceDetails(
        @Query("place_id") placeId: String?,
        @Query("key") apiKey: String?
    ): Call<PlaceDetailsResponse>


    @GET("maps/api/place/textsearch/json")
    fun searchPlacesByName(
        @Query("query") query: String,
        @Query("key") apiKey: String
    ): Call<PlacesSearchResponse>
}