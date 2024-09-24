package com.example.exploreease.data.room.entity

import android.util.Log
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route


data class RoutesWithPlaces(
    @Embedded val route: RouteEntity,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "placeId",
        associateBy = Junction(PlacesRouteCrossRef::class)
    )
    val places: List<PlaceEntity>,
) {
    companion object {

        fun parseToRoute(
            routeEntity: RoutesWithPlaces,
            relations: List<PlacesRouteCrossRef>
        ): Route {
            Log.d("44444444444444","relations ="+relations)
            val list = routeEntity.places
            var parsedList = ArrayList<Place>()
            var parsedMapWithSequence = HashMap<Int, Place>()
            for (place in list) {
                var sequence = 0
                for (relation in relations) {
                    if (place.placeId == relation.placeId && routeEntity.route.routeId == relation.routeId){

                            sequence = relation.sequenceNumber
                            break
                        }
                }
                //   parsedList.add(PlaceEntity.parseToPlace(place))
                parsedMapWithSequence[sequence] = PlaceEntity.parseToPlace(place)
            }
            Log.d("44444444444444","unsorted ${routeEntity.route.routeId} ="+parsedMapWithSequence)
            val sortedMap = parsedMapWithSequence.toSortedMap()
           //  Log.d("44444444444444","sortedMap ="+sortedMap)
            for (key in sortedMap.keys) {
                val place = parsedMapWithSequence.get(key);
                if (place != null)
                    parsedList.add(place)
            }
            Log.d("44444444444444","parsedList ${routeEntity.route.routeId}="+parsedList)

            val startPoint =
                if (parsedList.isNullOrEmpty()) Place.EMPTY_PLACE else parsedList.get(0)

            return Route(
                routeEntity.route.routeId,
                routeEntity.route.name,
                routeEntity.route.distance,
                parsedList,
                startPoint,
                routeEntity.route.isCompleted
            )
        }
    }
}
