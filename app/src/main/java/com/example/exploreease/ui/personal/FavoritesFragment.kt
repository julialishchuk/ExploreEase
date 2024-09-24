package com.example.exploreease.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.ExploreApplication
import com.example.exploreease.databinding.FragmentFavoritesBinding
import com.example.exploreease.ui.adapter.FavoritesPlacesAdapter


class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    private lateinit var favoritesViewModel : PersonalPreferencesViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesAdapter: FavoritesPlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)


        val favoritesManager = ExploreApplication.self().getFavoritesManager()
        val routesManager = ExploreApplication.self().getRoutesManager()
        val activityLogManager = ExploreApplication.self().getActivityLogManager()
        val viewModelFactory = PersonalPreferencesViewModel.Factory(favoritesManager, routesManager,activityLogManager)
        favoritesViewModel= ViewModelProvider(this, viewModelFactory).get(PersonalPreferencesViewModel::class.java)



        return _binding!!.root
        //return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.getFavorites()

        recyclerView = _binding?.rvPlaces!!
        recyclerView.layoutManager = LinearLayoutManager(activity)

        favoritesViewModel.favoritesPlaces.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                placesAdapter = FavoritesPlacesAdapter(it)
                recyclerView.adapter = placesAdapter
            }

        }
    }


}