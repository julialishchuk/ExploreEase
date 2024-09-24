package com.example.exploreease.domain.entity


data class Route(
    val id: Int,
    val name: String,
    val distance: Double,
    val points: List<Place>,
    val startingPoint: Place,
    val isCompleted : Boolean
)