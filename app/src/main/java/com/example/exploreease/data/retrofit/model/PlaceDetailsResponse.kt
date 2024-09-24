package com.example.exploreease.data.retrofit.model

import com.google.gson.annotations.SerializedName
import com.google.maps.model.LatLng

data class PlaceDetailsResponse(
    @SerializedName("result")
    val result: PlaceDetails?,
    val status: String? = null
)

data class PlaceDetails(
    @SerializedName("place_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("formatted_address")
    val formattedAddress: String,
    // the part of description
    @SerializedName("types")
    val types: List<String>,
    // the part of description
    @SerializedName("opening_hours")
    val openingHours: PlaceOpeningHours,
    @SerializedName("geometry")
    val geometry: PlaceGeometry


)

data class PlaceOpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean,
    @SerializedName("weekday_text")
    val weekDayText: List<String>
)

data class PlaceGeometry(
    @SerializedName("location")
    val latLng: LatLng
)

