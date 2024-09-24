package com.example.exploreease.ui.personal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.ExploreApplication
import com.example.exploreease.databinding.FragmentRouteDetailsBinding
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.ui.OnRouteDetailsClickListener
import com.example.exploreease.ui.adapter.PlacesInRouteAdapter
import com.example.exploreease.ui.adapter.RoutesAdapter


class RouteDetailsFragment (): Fragment(), OnRouteDetailsClickListener {

    private lateinit var routePosition: String

    private var _binding: FragmentRouteDetailsBinding? = null

    private lateinit var viewModel : PersonalPreferencesViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesAdapter: PlacesInRouteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d("111111111111111", "position"+routePosition)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRouteDetailsBinding.inflate(inflater, container, false)

        routePosition = arguments?.getString("routePosition").toString();
        Log.d("111111111111111", "position"+routePosition)




        val favoritesManager = ExploreApplication.self().getFavoritesManager()
        val routesManager = ExploreApplication.self().getRoutesManager()
        val activityLogManager = ExploreApplication.self().getActivityLogManager()
        val viewModelFactory = PersonalPreferencesViewModel.Factory(favoritesManager, routesManager,activityLogManager)
        viewModel= ViewModelProvider(this, viewModelFactory).get(PersonalPreferencesViewModel::class.java)

        _binding!!.tvRouteName.setText(viewModel.routes.value?.get(routePosition.toInt())?.name)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRoutes()
        recyclerView = _binding?.rvPlacesInRoute!!
        recyclerView.layoutManager = LinearLayoutManager(activity)


        viewModel.routes.observe(viewLifecycleOwner){
            var places = viewModel.routes.value?.get(routePosition.toInt())?.points
            if (places.isNullOrEmpty()) places = ArrayList<Place>()
            Log.d("111111111111111", places.toString())
            placesAdapter = PlacesInRouteAdapter(places = places,this)
            recyclerView.adapter=placesAdapter
            _binding!!.tvRouteName.setText(viewModel.routes.value?.get(routePosition.toInt())?.name)
        }


        _binding!!.btSaveName.setOnClickListener {
            Log.d("55555555555", "setOnClickListener ")
            if (_binding!!.tvRouteName.text?.isNotEmpty() == true) {
                val route = viewModel.routes.value?.get(routePosition.toInt())
                if (route != null)
                    viewModel.changeRouteName(route.id, _binding!!.tvRouteName.text.toString())
            } else Toast.makeText(context, "Please enter non-empty route name", Toast.LENGTH_SHORT)
                .show()

            _binding!!.tvRouteName.clearFocus()
        }


        

    }

    override fun onButtonRemovePlaceClick(position: Int) {
        val route = viewModel.routes.value?.get(routePosition.toInt())
        var place = route?.points?.get(position)
        if (route != null && place != null) {
            Log.d("444444444444", "onButtonRemovePlaceClick = " + route.id + " " + place.name)
            viewModel.removePlaceFromRoute(route.id, place.id)
        }


    }


}