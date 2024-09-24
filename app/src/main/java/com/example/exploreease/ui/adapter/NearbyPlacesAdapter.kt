package com.example.exploreease.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.R
import com.example.exploreease.domain.entity.Place


class NearbyPlacesAdapter(private val places: List<Place>) :
    RecyclerView.Adapter<NearbyPlacesAdapter.PlaceViewHolder>() {

    val resIds = ArrayList<Int>()

    init {
        resIds.add(R.drawable.first)
        resIds.add(R.drawable.sec)
        resIds.add(R.drawable.third)
        resIds.add(R.drawable.four)
        resIds.add(R.drawable.fifth)
        resIds.add(R.drawable.six)
        resIds.add(R.drawable.seven)
        resIds.add(R.drawable.eight)
        resIds.add(R.drawable.nine)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nearby_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place,resIds[position])
    }

    override fun getItemCount(): Int = places.size

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_place_name)
        private val address: TextView = itemView.findViewById(R.id.tv_place_address)
        private val image: ImageView = itemView.findViewById(R.id.iv_icon)

        fun bind(place: Place, res : Int) {
            name.text = place.name
            address.text = place.address
            image.setImageResource(res)
        }
    }
}