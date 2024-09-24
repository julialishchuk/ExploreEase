package com.example.exploreease.data.repository

import android.util.Log
import com.example.exploreease.data.room.dao.RouteDao
import com.example.exploreease.data.room.entity.PlaceEntity
import com.example.exploreease.data.room.entity.PlacesRouteCrossRef
import com.example.exploreease.data.room.entity.RouteEntity
import com.example.exploreease.data.room.entity.RoutesWithPlaces
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route
import com.example.exploreease.domain.repository.RoutesRepository
import kotlin.random.Random

class RoutesRepositoryImpl(
    private val routeDao: RouteDao
) : RoutesRepository {
    override fun getCompletedRoutes(): List<Route> {
        val routesEntity = routeDao.getCompletedRoutesWithPlaces()
        val parsedRouteList = ArrayList<Route>()
        var relations = routeDao.getRelations()
        for (routeEntity in routesEntity) {
            parsedRouteList.add(RoutesWithPlaces.parseToRoute(routeEntity,relations))
        }
        Log.d("22222222222222", "getAllRoutes() " + parsedRouteList)
        return parsedRouteList
    }


    override fun getActiveRoutes(): List<Route> {
        val routesEntity = routeDao.getActiveRoutesWithPlaces()
        Log.d("44444444444444","routesEntity = "+routesEntity)
        val parsedRouteList = ArrayList<Route>()
        var relations = routeDao.getRelations()
        for (routeEntity in routesEntity) {
            parsedRouteList.add(RoutesWithPlaces.parseToRoute(routeEntity, relations))
        }
        Log.d("22222222222222", "getActiveRoutes() " + parsedRouteList)
        return parsedRouteList
    }

    override fun createRoute(route: Route) {
        routeDao.insertRoute(RouteEntity.parseFromRoute(route,false))
    }

    override fun removeRoute(route: Route) {
        routeDao.deletePlace(RouteEntity.parseFromRoute(route,false) )
    }

    override fun addStopToRoute(place: Place, routeId: Int) {
        routeDao.insertPlace(PlaceEntity.parseFromPlace(place, false))
        //TODO remove random, set correct
        var position = 0
        if (routeDao.getRelations().isNotEmpty()) position =
            routeDao.getRelations().last().sequenceNumber
        Log.d("44444444444", "last sequenceNumber is $position")
        routeDao.insertCrossRef(PlacesRouteCrossRef(routeId, place.id, position + 1))
        Log.d("22222222222222", "addStopToRoute() " + place.id + "route=" + routeId)
    }

    override fun removeStopFromRoute(routeId: Int, placeId: String) {
        Log.d("44444444444", "getRelations before "+routeDao.getRelations());
        routeDao.deleteCrossRef(routeId, placeId = placeId)
        Log.d("44444444444", "getRelations after "+routeDao.getRelations());
    }

    override fun completeRoute(routeId: Int, isCompleted: Boolean) {
        routeDao.updateIsCompleted(routeId, isCompleted)
    }

    override fun changeRouteName(routeId: Int, newName: String) {
        routeDao.updateRouteName(routeId, newName)
    }


}