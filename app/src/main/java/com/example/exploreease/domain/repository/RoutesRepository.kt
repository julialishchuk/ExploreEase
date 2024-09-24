package com.example.exploreease.domain.repository

import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route

interface RoutesRepository {
    fun getCompletedRoutes(): List<Route>
    fun getActiveRoutes(): List<Route>
    fun createRoute(route: Route)
    fun removeRoute(route: Route)
    fun addStopToRoute(place: Place, routeId: Int)
    fun removeStopFromRoute(routeId: Int, placeId: String)
    fun completeRoute(routeId: Int, isCompleted: Boolean)
    fun changeRouteName(routeId: Int, newName: String)
}