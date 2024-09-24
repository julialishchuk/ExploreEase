package com.example.exploreease.data.retrofit

import com.example.exploreease.data.retrofit.service.GeoApiService
import com.example.exploreease.data.retrofit.service.PlacesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MapsApiInstance {
    private const val BASE_URL = "https://maps.googleapis.com/"
    public const val API_KEY= "" //Move this as BuildConfig

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val geoApiService: GeoApiService by lazy {
        retrofit.create(GeoApiService::class.java)
    }

    val placesApiService: PlacesApiService by lazy {
        retrofit.create(PlacesApiService::class.java)
    }
}
