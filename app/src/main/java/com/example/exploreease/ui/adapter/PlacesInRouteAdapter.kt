package com.example.exploreease.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.R
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.ui.OnRouteDetailsClickListener


class PlacesInRouteAdapter(private val places: List<Place>, private val listener: OnRouteDetailsClickListener) :
    RecyclerView.Adapter<PlacesInRouteAdapter.PlaceInRouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceInRouteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_place_in_route, parent, false)
        return PlaceInRouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceInRouteViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place, position,listener)
    }

    override fun getItemCount(): Int = places.size

    class PlaceInRouteViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val placeName: TextView = itemView.findViewById(R.id.tv_place_name)
        private val placeAddress: TextView = itemView.findViewById(R.id.tv_place_address)
        private val btRemovePlace: TextView = itemView.findViewById(R.id.bt_remove_place)


        fun bind(place: Place, adapterPosition: Int, listener: OnRouteDetailsClickListener) {
            placeName.text = place.name
            placeAddress.text = place.address

            btRemovePlace.setOnClickListener{
                listener.onButtonRemovePlaceClick(adapterPosition)
            }


//            btComplete.setOnClickListener {
//                listener.onButtonCompleteClick(adapterPosition)
//            }
//
//            btDraw.setOnClickListener {
//                listener.onButtonDrawClick(adapterPosition)
//            }
//
//            btRemoveRoute.setOnClickListener {
//                listener.onButtonRemoveClick(adapterPosition)
//            }
//
//            routeName.setOnClickListener{
//                listener.onButtonRouteDetailsClick(adapterPosition)
//            }
        }
    }
}