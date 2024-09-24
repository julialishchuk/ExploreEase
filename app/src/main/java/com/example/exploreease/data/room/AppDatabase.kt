package com.example.exploreease.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.exploreease.data.room.dao.PlaceDao
import com.example.exploreease.data.room.dao.RouteDao
import com.example.exploreease.data.room.entity.PlaceEntity
import com.example.exploreease.data.room.entity.PlacesRouteCrossRef
import com.example.exploreease.data.room.entity.RouteEntity

@Database(
    entities = [PlaceEntity::class, RouteEntity::class, PlacesRouteCrossRef::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}