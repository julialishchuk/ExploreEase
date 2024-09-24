package com.example.exploreease.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.ExploreApplication
import com.example.exploreease.databinding.FragmentHomeBinding
import com.example.exploreease.ui.adapter.FavoritesPlacesAdapter
import com.example.exploreease.ui.adapter.NearbyPlacesAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var placesAdapter: NearbyPlacesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val activityLogManager = ExploreApplication.self().getActivityLogManager()
        val viewModelFactory = HomeViewModel.Factory(activityLogManager)
        val homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        recyclerView = _binding?.rvNearbyPlaces!!
        recyclerView.layoutManager = LinearLayoutManager(activity)

        placesAdapter = NearbyPlacesAdapter(homeViewModel.getNearbyPlaces())
        recyclerView.adapter = placesAdapter


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}