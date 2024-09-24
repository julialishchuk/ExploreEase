package com.example.exploreease.ui.adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.R
import com.example.exploreease.domain.entity.Route
import com.example.exploreease.ui.OnRouteItemClickListener


class RoutesAdapter(private val routes: List<Route>, private val listener: OnRouteItemClickListener) :
    RecyclerView.Adapter<RoutesAdapter.RouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val route = routes[position]
        holder.bind(route, position)
    }

    override fun getItemCount(): Int = routes.size

    class RouteViewHolder(itemView: View, val listener: OnRouteItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val routeName: TextView = itemView.findViewById(R.id.tv_route_name)
        private val startPoint: TextView = itemView.findViewById(R.id.tv_route_starting_point)
        private val label: TextView = itemView.findViewById(R.id.tv_start_from)

        private val btComplete: TextView = itemView.findViewById(R.id.bt_complete_route)
        private val btDraw: TextView = itemView.findViewById(R.id.bt_draw_route)
        private val btRemoveRoute: TextView = itemView.findViewById(R.id.bt_remove_route)


        fun bind(route: Route, adapterPosition: Int) {
            routeName.text = route.name
            if (route.startingPoint.name.isNullOrEmpty()) {
                label.visibility = View.GONE
                startPoint.visibility = View.GONE
            } else startPoint.text = route.startingPoint.name


            btComplete.setOnClickListener {
                listener.onButtonCompleteClick(adapterPosition)
            }

            btDraw.setOnClickListener {
                listener.onButtonDrawClick(adapterPosition)
            }

            btRemoveRoute.setOnClickListener {
                listener.onButtonRemoveClick(adapterPosition)
            }

            routeName.setOnClickListener{
                listener.onButtonRouteDetailsClick(adapterPosition)
            }
        }
    }
}