package com.example.exploreease.ui

interface OnRouteItemClickListener {
    fun onButtonDrawClick(position: Int)
    fun onButtonRemoveClick(position: Int)
    fun onButtonCompleteClick(position: Int)

    fun onButtonRouteDetailsClick(position: Int)

}