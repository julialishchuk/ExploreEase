package com.example.exploreease.domain.manager

import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route
import com.example.exploreease.domain.repository.RoutesRepository

class RoutesManager(private val routesRepo: RoutesRepository) {

    fun getActiveRoutes(): List<Route> {
        return routesRepo.getActiveRoutes()
    }

    fun createRoute(route: Route) {
        return routesRepo.createRoute(route)
    }

    fun removeRoute(route: Route) {
        return routesRepo.removeRoute(route)
    }

    fun addStopToRoute(place: Place, routeId: Int) {
        return routesRepo.addStopToRoute(place, routeId)
    }

    fun removeStopFromRoute(routeId: Int, placeId: String) {
        return routesRepo.removeStopFromRoute(routeId, placeId)
    }

    fun completeRoute(routeId: Int, isCompleted: Boolean){
        return routesRepo.completeRoute(routeId, isCompleted)
    }

    fun changeRouteName(routeId: Int, newName: String) {
        return routesRepo.changeRouteName(routeId, newName)
    }

}
