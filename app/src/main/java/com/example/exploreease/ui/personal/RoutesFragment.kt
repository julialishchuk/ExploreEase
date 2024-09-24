package com.example.exploreease.ui.personal

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exploreease.ExploreApplication
import com.example.exploreease.R
import com.example.exploreease.databinding.FragmentRoutesBinding
import com.example.exploreease.ui.OnRouteItemClickListener
import com.example.exploreease.ui.adapter.RoutesAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView


class RoutesFragment : Fragment() , OnRouteItemClickListener{

    private var _binding: FragmentRoutesBinding? = null

    private lateinit var viewModel : PersonalPreferencesViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var routesAdapter: RoutesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoutesBinding.inflate(inflater, container, false)


        val favoritesManager = ExploreApplication.self().getFavoritesManager()
        val routesManager = ExploreApplication.self().getRoutesManager()
        val activityLogManager = ExploreApplication.self().getActivityLogManager()
        val viewModelFactory = PersonalPreferencesViewModel.Factory(favoritesManager, routesManager,activityLogManager)
        viewModel= ViewModelProvider(this, viewModelFactory).get(PersonalPreferencesViewModel::class.java)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRoutes()
        recyclerView = _binding?.rvRoutes!!
        recyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.routes.observe(viewLifecycleOwner){
            routesAdapter = RoutesAdapter(it, this)
            recyclerView.adapter = routesAdapter
        }

    }

    override fun onStart() {
        super.onStart()

//        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
//        navView.selectedItemId = R.id.navigation_personal_preferences
    }

    override fun onButtonDrawClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("routePosition", position.toString())
        findNavController().navigate(R.id.action_routes_to_navigationSearchPlace, bundle)
    }

    override fun onButtonRemoveClick(position: Int) {

        showConfirmationDialog(requireContext(),
            "Remove route",
            "Are you sure you want to remove the route?",
            "Yes",
            "Cancel",
            {
                viewModel.routes.value?.get(position)?.let { viewModel.removeRoute(it) }
            },
            {}
            )

    }

    override fun onButtonCompleteClick(position: Int) {
        viewModel.routes.value?.get(position)?.let { viewModel.completeRoute(it) }
    }

    override fun onButtonRouteDetailsClick(position: Int) {
        val bundle = Bundle()
        bundle.putString("routePosition", position.toString())
        findNavController().navigate(R.id.action_routes_to_navigationRouteDetails, bundle)
    }


    fun showConfirmationDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveButtonClick: () -> Unit,
        onNegativeButtonClick: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(positiveButtonText) { dialog, _ ->
            onPositiveButtonClick()
            dialog.dismiss()
        }

        builder.setNegativeButton(negativeButtonText) { dialog, _ ->
            onNegativeButtonClick()
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }


}