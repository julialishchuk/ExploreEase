package com.example.exploreease.data.room.entity

import androidx.room.Entity
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route

@Entity(tableName = "places_routes_cross_ref",
    primaryKeys = ["routeId", "placeId"]
)
data class PlacesRouteCrossRef(
    val routeId: Int,
    val placeId: String,
    val sequenceNumber: Int
)