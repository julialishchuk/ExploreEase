package com.example.exploreease.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.R
import com.example.exploreease.domain.entity.Place


class FavoritesPlacesAdapter(private val places: List<Place>) :
    RecyclerView.Adapter<FavoritesPlacesAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
    }

    override fun getItemCount(): Int = places.size

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_place_name)
        private val address: TextView = itemView.findViewById(R.id.tv_place_address)

        fun bind(place: Place) {
            name.text = place.name
            address.text = place.address
        }
    }
}