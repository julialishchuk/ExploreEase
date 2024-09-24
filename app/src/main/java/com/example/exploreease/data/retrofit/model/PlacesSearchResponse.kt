package com.example.exploreease.data.retrofit.model

import com.google.gson.annotations.SerializedName

class PlacesSearchResponse {
    @SerializedName("results")
    var results: List<PlaceDetails>? = null

    @SerializedName("status")
    var status: String? = null
}
