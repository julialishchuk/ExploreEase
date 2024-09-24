package com.example.exploreease.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class GeocodeResponse (
    val results: List<GeocodePoint>? = null,
    val status: String? = null
)


data class GeocodePoint (
    @SerializedName("place_id")
    val placeId: String? = null,
    @SerializedName("formatted_address")
    val formattedAddress: String? = null
)
