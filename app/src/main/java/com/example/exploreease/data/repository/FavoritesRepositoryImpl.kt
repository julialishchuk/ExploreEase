package com.example.exploreease.data.repository

import com.example.exploreease.data.room.dao.PlaceDao
import com.example.exploreease.data.room.entity.PlaceEntity
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.repository.FavoritesRepository

class FavoritesRepositoryImpl(
    private val placeDao: PlaceDao
) : FavoritesRepository {


    override fun getAllFavorites(): List<Place> {
        val placeEntities = placeDao.getFavoritePlaces()
        val places = ArrayList<Place>()

        for (placeEntity in placeEntities) {
            val p = PlaceEntity.parseToPlace(placeEntity)
            places.add(p)
        }

        return places
    }

    override fun isFavorite(place: Place): Boolean {
        val favorites = getAllFavorites()
        for (placeEntity in favorites) {
            if (placeEntity.id == place.id)
                return true
        }
        return false
    }

    override fun addToFavorite(place: Place) {
        placeDao.insertPlace(PlaceEntity.parseFromPlace(place, true))
    }

    override fun removeFromFavorite(place: Place) {
        placeDao.deletePlace((PlaceEntity.parseFromPlace(place, true)))
    }
}