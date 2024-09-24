package com.example.exploreease

import android.app.Application
import android.util.Log
import com.example.exploreease.data.repository.FavoritesRepositoryImpl
import com.example.exploreease.data.repository.PlacesRepositoryImpl
import com.example.exploreease.data.repository.RoutesRepositoryImpl
import com.example.exploreease.data.retrofit.MapsApiInstance
import com.example.exploreease.data.room.AppDatabase
import com.example.exploreease.data.room.entity.PlaceEntity
import com.example.exploreease.domain.manager.ActivityLogManager
import com.example.exploreease.domain.manager.FavoritesManager
import com.example.exploreease.domain.manager.PlacesManager
import com.example.exploreease.domain.manager.RoutesManager

class ExploreApplication : Application() {

    companion object {
        private lateinit var sSelf: ExploreApplication

        fun self(): ExploreApplication {
            return sSelf
        }
    }

    init {
        sSelf = this
    }

    private lateinit var placesManager: PlacesManager
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var routesManager: RoutesManager
    private lateinit var activityLogManager: ActivityLogManager



    override fun onCreate() {
        super.onCreate()

        init()

//        PlacesRepositoryImpl(
//            MapsApiInstance.geoApiService,
//            MapsApiInstance.placesApiService).test(LatLng(-125.8655112697085, 111.1971156302915))

    }

    private fun tryDB(){
        Thread(
            Runnable {

                val db = AppDatabase.getDatabase(this)

// Assuming you have a PlaceEntity object representing the place you want to add
                val place = PlaceEntity("1",name = "Example Place", address = "123 Example St", 5.0,5.0,isFav = true)

// Insert the place into the database using the PlaceDao
               // db.placeDao().insertPlace(place)

//                db.placeDao().insertPlace(PlaceEntity("2",name = "Example2 Place", address = "123 Example St", 5.0,5.0,isFav = false))
//                db.placeDao().insertPlace(PlaceEntity("3",name = "Example3 Place", address = "123 Example St", 5.0,5.0,isFav = false))
//                db.placeDao().insertPlace(PlaceEntity("4",name = "Example4 Place", address = "123 Example St", 5.0,5.0,isFav = true))
//                db.placeDao().insertPlace(PlaceEntity("5",name = "Example5 Place", address = "123 Example St", 5.0,5.0,isFav = true))

                Log.d("UHAUEAAAAAAAAA",db.placeDao().getFavoritePlaces().toString())
            }
        ).start()

    }

    private fun init() {
        //AppDatabase.getDatabase(this)
        tryDB()

        val placesRepoImpl =
            PlacesRepositoryImpl(MapsApiInstance.geoApiService, MapsApiInstance.placesApiService)
        placesManager = PlacesManager(placesRepoImpl)

        val favoritesRepoImpl = FavoritesRepositoryImpl(AppDatabase.getDatabase(this).placeDao())
        favoritesManager = FavoritesManager(favoritesRepoImpl)

        val routesRepoImpl = RoutesRepositoryImpl(AppDatabase.getDatabase(this).routeDao())
        routesManager = RoutesManager(routesRepoImpl)

        activityLogManager = ActivityLogManager(routesRepoImpl)

    }

    fun getPlacesManager(): PlacesManager {
        return placesManager
    }

    fun getFavoritesManager(): FavoritesManager {
        return favoritesManager
    }

    fun getRoutesManager(): RoutesManager {
        return routesManager
    }

    fun getActivityLogManager(): ActivityLogManager {
        return activityLogManager
    }


}