package com.example.exploreease.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route
import com.google.maps.model.LatLng

@Entity(tableName = "routes")
data class RouteEntity(
    @PrimaryKey(autoGenerate = true) val routeId: Int = 0,
    val name: String,
    val distance: Double,
    val isCompleted: Boolean
){
    companion object {
        fun parseFromRoute(route: Route, isCompleted: Boolean): RouteEntity {
            return RouteEntity(
                route.id,
                route.name,
                route.distance,
                isCompleted
            )
        }
//
//        fun parseToRoute(routeEntity: RouteEntity): Route {
//            val list = routeEntity.
//
//            return Route(
//                routeEntity.routeId,
//                routeEntity.name,
//                routeEntity.distance,
//                ArrayList<Place>(),
//
//
//
//            )
//        }
    }
}