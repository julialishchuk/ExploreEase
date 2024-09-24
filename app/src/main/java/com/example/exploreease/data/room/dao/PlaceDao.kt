package com.example.exploreease.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exploreease.data.room.entity.PlaceEntity

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places WHERE isFav = 1")
    fun getFavoritePlaces(): List<PlaceEntity>

    @Query("SELECT * FROM places")
    fun getAllPlaces(): List<PlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: PlaceEntity)

    @Delete
    fun deletePlace(place: PlaceEntity)
}
