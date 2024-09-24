package com.example.exploreease.domain.entity

import android.location.Address

data class Place(
    val id: String,
    val name: String,
    val coordinates: com.google.maps.model.LatLng?,
    val address: String,
    val description: String
)
{
    companion object{
        val EMPTY_PLACE= Place("", "", com.google.maps.model.LatLng(6.7, 6.7), "", "")

    }
}