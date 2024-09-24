package com.example.exploreease.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.exploreease.data.room.entity.PlaceEntity
import com.example.exploreease.data.room.entity.PlacesRouteCrossRef
import com.example.exploreease.data.room.entity.RouteEntity
import com.example.exploreease.data.room.entity.RoutesWithPlaces

@Dao
interface RouteDao {

    @Transaction
    @Query("SELECT * FROM routes WHERE isCompleted = 1")
    fun getCompletedRoutesWithPlaces(): List<RoutesWithPlaces>

    @Transaction
    @Query("SELECT * FROM routes  WHERE isCompleted = 0")
    fun getActiveRoutesWithPlaces(): List<RoutesWithPlaces>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoute(route: RouteEntity)

    @Query("UPDATE routes SET name = :newName WHERE routeId = :routeId")
    fun updateRouteName(routeId: Int, newName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: PlaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCrossRef(crossRef: PlacesRouteCrossRef)

    @Query("SELECT * FROM places_routes_cross_ref")
    fun getRelations(): List<PlacesRouteCrossRef>

    @Query(
        "DELETE FROM places_routes_cross_ref WHERE routeId = :routeId AND placeId = :placeId"
    )
    fun deleteCrossRef(routeId: Int, placeId: String)

    @Delete
    fun deletePlace(route: RouteEntity)

    @Query("UPDATE routes SET isCompleted = :isCompleted WHERE routeId = :routeId")
    fun updateIsCompleted(routeId: Int, isCompleted: Boolean)
}