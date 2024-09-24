package com.example.exploreease.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.exploreease.ExploreApplication
import com.example.exploreease.databinding.FragmentActivityLogBinding
import com.example.exploreease.databinding.FragmentFavoritesBinding
import com.example.exploreease.ui.adapter.FavoritesPlacesAdapter
import kotlin.random.Random


class ActivityLogFragment : Fragment() {

    private var _binding: FragmentActivityLogBinding? = null

    private lateinit var viewModel : PersonalPreferencesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentActivityLogBinding.inflate(inflater, container, false)


        val favoritesManager = ExploreApplication.self().getFavoritesManager()
        val routesManager = ExploreApplication.self().getRoutesManager()
        val activityLogManager = ExploreApplication.self().getActivityLogManager()
        val viewModelFactory = PersonalPreferencesViewModel.Factory(favoritesManager, routesManager,activityLogManager)
        viewModel= ViewModelProvider(this, viewModelFactory).get(PersonalPreferencesViewModel::class.java)
        viewModel.getCompletedRoutes()


        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.completedRoutes.observe(viewLifecycleOwner) {
            if (it != null && it.isNotEmpty()) {
                val routeNum = it.size
                var placeNum = 0
                for (routePlaces in it) {
                    placeNum += routePlaces.points.size
                }
                var distance = 0
                if (it.size==1) distance= 15//Random.nextInt(10, 100)
                if (it.size==2) distance= 47//Random.nextInt(100, 150)
                if (it.size==3) distance= 112//Random.nextInt(150, 220)
                if (it.size==4) distance= 148//Random.nextInt(220, 300)
                if (it.size>4) distance= 156//Random.nextInt(300, 1000)
                _binding!!.tvRoutesNumber.text = routeNum.toString()
                _binding!!.tvPlacesNumber.text = placeNum.toString()
                _binding!!.tvDistance.text = distance.toString()
            }

        }
    }

    /*
    загальну дистанцію, кількість завершених маршрутів та відвіданих місць.

     */

}