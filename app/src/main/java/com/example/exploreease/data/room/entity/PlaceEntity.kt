package com.example.exploreease.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.exploreease.domain.entity.Place
import com.google.maps.model.LatLng

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val placeId: String,
    val name: String,
    val address: String,
    val latitude: Double?,
    val longitude: Double?,
    val isFav: Boolean
)
{
    companion object {
        fun parseFromPlace(place: Place, isFav: Boolean): PlaceEntity {
            return PlaceEntity(
                place.id,
                place.name,
                place.address,
                place.coordinates?.lat,
                place.coordinates?.lng,
                isFav

            )
        }

        fun parseToPlace(placeEntity: PlaceEntity): Place {
            var latLng: LatLng? = null
            if (placeEntity.latitude != null && placeEntity.longitude != null) {
                latLng = LatLng(placeEntity.latitude, placeEntity.longitude)
            }
            return Place(
                placeEntity.placeId,
                placeEntity.name,
                latLng,
                placeEntity.address,
                ""
            )
        }
    }
}
