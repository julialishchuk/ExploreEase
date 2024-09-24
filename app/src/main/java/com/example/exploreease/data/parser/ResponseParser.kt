package com.example.exploreease.data.parser

import com.example.exploreease.data.retrofit.model.GeocodeResponse
import com.example.exploreease.data.retrofit.model.PlaceDetailsResponse
import com.example.exploreease.data.retrofit.model.PlacesSearchResponse
import com.example.exploreease.domain.entity.Place

object ResponseParser {

    fun parsePlaceDetailsResponse(placeDetailsResponse: PlaceDetailsResponse): Place? {
        if (placeDetailsResponse.result == null) {
            return null
        }
        val placeDetails = placeDetailsResponse.result
        var description = "" + placeDetails.name
        if (placeDetails.types.isNotEmpty()) {
            description += "\nPlace type: "

        }

        for ((index,p) in placeDetails.types.withIndex()){
            if (index==0)     description+="$p "
            else  description+=", $p "
        }
//        if (placeDetails.openingHours != null && placeDetails.openingHours.weekDayText.isNotEmpty()) {
//            description +=
//                ",\n Place opening hours: ${placeDetails.openingHours.weekDayText}"
//        }

        return Place(
            placeDetails.id,
            placeDetails.name,
            placeDetails.geometry.latLng,
            placeDetails.formattedAddress,
            description
        )
    }

    fun parsePlaceSearchResponse(placesSearchResponse: PlacesSearchResponse): Place? {
        if (placesSearchResponse.results.isNullOrEmpty()) {
            return null
        }
        val placeDetails = placesSearchResponse.results!![0]
        var description = "Place name: " + placeDetails.name
        if (placeDetails.types.isNotEmpty()) {
            description += ",\n Place type: ${placeDetails.types}"

        }
        if (placeDetails.openingHours != null && !placeDetails.openingHours.weekDayText.isNullOrEmpty()) {
            description +=
                ",\n Place opening hours: ${placeDetails.openingHours.weekDayText}"
        }

        return Place(
            placeDetails.id,
            placeDetails.name,
            placeDetails.geometry.latLng,
            placeDetails.formattedAddress,
            description
        )
    }






    fun parsePlaceIdFromGeocodeResponse(geocodeResponse: GeocodeResponse): String? {
        if (geocodeResponse.results.isNullOrEmpty()) {
            return null
        }
        return geocodeResponse.results[0].placeId
    }

}