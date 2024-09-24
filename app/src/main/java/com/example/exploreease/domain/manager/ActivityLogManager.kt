package com.example.exploreease.domain.manager

import com.example.exploreease.domain.entity.Route
import com.example.exploreease.domain.repository.RoutesRepository

class ActivityLogManager(private val routesRepo: RoutesRepository)
{
    fun getCompletedRoutes(): List<Route>{
        return routesRepo.getCompletedRoutes()
    }
}