package com.example.exploreease.domain.repository

import com.example.exploreease.domain.entity.Place

interface FavoritesRepository {
    fun getAllFavorites(): List<Place>
    fun isFavorite(place: Place): Boolean
    fun addToFavorite(place: Place)
    fun removeFromFavorite(place: Place)
}