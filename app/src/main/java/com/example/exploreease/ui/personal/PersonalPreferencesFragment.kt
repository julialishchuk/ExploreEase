package com.example.exploreease.ui.personal


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.exploreease.ExploreApplication
import com.example.exploreease.R
import com.example.exploreease.databinding.FragmentPersonalPreferencesBinding
import com.example.exploreease.ui.search.SearchPlaceViewModel


class PersonalPreferencesFragment : Fragment() {

private var _binding: FragmentPersonalPreferencesBinding? = null

  private val binding get() = _binding!!
    private lateinit var personalPrefViewModel : PersonalPreferencesViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

      val favoritesManager = ExploreApplication.self().getFavoritesManager()
      val routesManager = ExploreApplication.self().getRoutesManager()
      val activityLogManager = ExploreApplication.self().getActivityLogManager()
      val viewModelFactory = PersonalPreferencesViewModel.Factory(favoritesManager, routesManager,activityLogManager)
      personalPrefViewModel=ViewModelProvider(this, viewModelFactory).get(PersonalPreferencesViewModel::class.java)

//      personalPrefViewModel?.selectedPlaceLiveData?.observe(viewLifecycleOwner){
//          if (it!=null && !searchPlaceViewModel?.isPlaceHandled!!){
//              searchPlaceViewModel?.isPlaceHandled = true
//              showPlaceDetailsDialog(it)
//              //Toast.makeText(context,"Peremoga "+it.name, Toast.LENGTH_LONG).show()
//          }
//      }


    _binding = FragmentPersonalPreferencesBinding.inflate(inflater, container, false)
    val root: View = binding.root

//    val textView: TextView = binding.textNotifications
//    personalPrefViewModel.text.observe(viewLifecycleOwner) {
//      textView.text = it
//    }

      init()
    return root
  }

    private fun init(){
        binding.buttonFavorites.setOnClickListener{
            findNavController().navigate(R.id.action_personalPreferences_to_navigationFavorites)
        }

        binding.buttonRoutes.setOnClickListener{
            findNavController().navigate(R.id.action_personalPreferences_to_navigationRoutes)
        }

        binding.buttonActivityLog.setOnClickListener{
            findNavController().navigate(R.id.action_personalPreferences_to_navigationActivity)
        }

    }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}