package com.example.exploreease.ui.search


import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.exploreease.ExploreApplication
import com.example.exploreease.R
import com.example.exploreease.databinding.FragmentSearchPlaceBinding
import com.example.exploreease.domain.entity.Place
import com.example.exploreease.domain.entity.Route
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class SearchPlaceFragment : Fragment(), OnMapReadyCallback {

    companion object {
        private final const val TAG = "SearchPlaceFragment"
    }

    private lateinit var mMap: GoogleMap

    private var _binding: FragmentSearchPlaceBinding? = null

    private var searchPlaceViewModel: SearchPlaceViewModel? = null

    private val binding get() = _binding!!

    private lateinit var routePositionArgument: String

    val myLocation = LatLng(49.86945050, 24.02526400)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPlaceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewModel()
        initMap()

        initSearchView()
        initCreateRoute()


        routePositionArgument = arguments?.getString("routePosition").toString();
        Log.d("111111111111111", "position" + routePositionArgument)
        if (routePositionArgument.isNotEmpty() && routePositionArgument.toInt() != -1) {
            Log.d("11111111111111", "call getWRouteAndDraw "+routePositionArgument)
           prepareToDrawRoute(routePositionArgument.toInt())
        }


        return root
    }

    override fun onStart() {
        super.onStart()
    }

    private fun initSearchView(){
        val placeSearchView =  binding.placeSearchView
        placeSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val location = placeSearchView.query.toString()
                searchLocationByLocationName(location)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
    }

    fun searchLocationByLocationName(location : String){
        var addressList : List<Address>? = null
        if (location.isNotEmpty()  ){
            Log.d(TAG, "Search is clicked: ")
            searchPlaceViewModel?.handlePlaceSearch(location)

            searchPlaceViewModel?.searchPlaceLiveData?.observe(viewLifecycleOwner){
                if (it?.coordinates==null) return@observe
                val latLong=LatLng(it.coordinates.lat, it.coordinates.lng)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(latLong).title(it.name))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong,10F))

                mMap.addCircle(
                    CircleOptions()
                        .center(myLocation)
                        .radius(50.0) // Radius in meters
                        .strokeColor(Color.BLUE)
                        .fillColor(0x220000FF) // 20% opaque blue fill
                        .strokeWidth(5f)
                )

            }
        }
    }

    private fun initViewModel() {
        val placesManager = ExploreApplication.self().getPlacesManager()
        val favoritesManager = ExploreApplication.self().getFavoritesManager()
        val routesManager = ExploreApplication.self().getRoutesManager()
        val viewModelFactory = SearchPlaceViewModel.Factory(placesManager, favoritesManager, routesManager)
        searchPlaceViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchPlaceViewModel::class.java)


        searchPlaceViewModel?.selectedPlaceLiveData?.observe(viewLifecycleOwner){
            if (it!=null && !searchPlaceViewModel?.isPlaceHandled!!){
                searchPlaceViewModel?.isPlaceHandled = true
                showPlaceDetailsDialog(it)
                //Toast.makeText(context,"Peremoga "+it.name, Toast.LENGTH_LONG).show()
            }
        }

        searchPlaceViewModel?.updateRoutesList()

    }

    private fun initMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
       // mMap.addMarker(MarkerOptions().position(myLocation).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))


        mMap.addCircle(
            CircleOptions()
                .center(myLocation)
                .radius(50.0) // Radius in meters
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF) // 20% opaque blue fill
                .strokeWidth(5f)
        )





        mMap.setOnMapClickListener {
            Log.d(TAG, "Map is clicked: ")
            searchPlaceViewModel?.handleMapClick(it)
        }

        mMap.setOnPoiClickListener { point ->
            Log.d(TAG, "Point of Interest clicked: " + point.name)
            searchPlaceViewModel?.handlePointOfInterestClick(point.placeId)
        }
    }

    private fun showPlaceDetailsDialog(place: Place) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_place_details, null)

        val textView = dialogView.findViewById<TextView>(R.id.textPlace)
        textView.text= "Address:\n\n"+place.address+"" +
                "\n\nDescription:\n\n"+place.description


        val spinner: Spinner = dialogView.findViewById(R.id.spinner_routes)
        var routes = searchPlaceViewModel?.routesLiveData?.value

        Log.d("IMPORTANT","routes: "+routes)
        val routeNames = ArrayList<String>( )
        if (routes != null) {
            for (r in routes) {
                routeNames.add(r.name)
            }
        }

        val adapter = activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, routeNames!!) }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        var selectedItem =-1

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                 selectedItem =position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        val addStopButton = dialogView.findViewById<TextView>(R.id.bt_add_stop)
        addStopButton.setOnClickListener {
            if (selectedItem != -1) {
                addStopToRoute(routes, place, selectedItem)
            }
        }

        val addToFav = dialogView.findViewById<TextView>(R.id.bt_add_to_favorites)
        addToFav.setOnClickListener {
            handleOnFavoriteClick(place)
        }

        searchPlaceViewModel?.isSelectedPlaceFavorite?.observe(viewLifecycleOwner) {
            if (it == true) {
                context?.let { it1 ->
                    androidx.core.content.ContextCompat.getColor(
                        it1,
                        com.example.exploreease.R.color.pink_accent
                    )
                }
                    ?.let { it2 -> addToFav.setBackgroundResource(R.drawable.ic_heart_yes_foreground) };
            } else {
                context?.let { it1 -> ContextCompat.getColor(it1, R.color.black) }
                    ?.let { it2 -> addToFav.setBackgroundResource(R.drawable.ic_heart_no_foreground)};
            }
        }


        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle(place.name)
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.setOnDismissListener {
            searchPlaceViewModel?.isSelectedPlaceFavorite?.removeObservers(viewLifecycleOwner)
        }
        dialog.show()

    }


    private fun addStopToRoute(routes: List<Route>?, place: Place, selectedItem: Int) {
        routes?.get(selectedItem)?.id?.let { routeId ->
            searchPlaceViewModel?.handleAddStopToRoute(
                place,
                routeId
            )
        }
        Toast.makeText(
            activity,
            "Stop added to route: " + routes?.get(selectedItem)?.name,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleOnFavoriteClick(place: Place){
        searchPlaceViewModel?.handleOnFavoriteClick(place)

    }


    private fun initCreateRoute(){
        binding.buttonCreateRoute.setOnClickListener{
            val dialogView = layoutInflater.inflate(R.layout.dialog_create_route, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Create a new route")
                .setPositiveButton("Create") { dialog, _ ->
                    val editText = dialogView.findViewById<TextView>(R.id.et_route_name)
                    val routeName=editText.text
                    if (!routeName.isNullOrEmpty()){
                        Log.d(TAG,"Create button is clicked")
                        searchPlaceViewModel?.handleCreateRoute(routeName.toString())
                        val string="New route created: $routeName"
                        Toast.makeText(activity,string, Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()

        }

    }


    private fun prepareToDrawRoute(routePosition: Int) {
        searchPlaceViewModel?.routesLiveData?.observe(viewLifecycleOwner) {
            if (routePositionArgument.isNullOrEmpty() || routePositionArgument == "-1") {
                return@observe
            }
            routePositionArgument="-1"
            val route = it[routePosition]
            Thread {
                val places = route?.points
                Log.d("5555555555555", "route = " + route)
                Log.d("5555555555555", "places = " + places)
                if (!places.isNullOrEmpty()) {
                    val waypoints = ArrayList<LatLng>()
                    for (place in places) {
                        if (place != places.first() && place != places.last())
                            waypoints.add(LatLng(place.coordinates!!.lat, place.coordinates.lng))
                    }
                    val first = LatLng(places.first().coordinates!!.lat, places.first().coordinates!!.lng)
                    val last = LatLng(places.last().coordinates!!.lat, places.last().coordinates!!.lng)
                    getRouteAndDraw(first,last, waypoints)
                }
            }.start()

        }
    }

    private fun getRouteAndDraw(originLatLng : LatLng, destinationLatLng: LatLng, waypoints: List<LatLng>) {
        val origin = "${originLatLng.latitude},${originLatLng.longitude}"
        val destination = "${destinationLatLng.latitude},${destinationLatLng.longitude}"
//        val wayPoint1 = LatLng(50.0, 30.0)
//        val wayPoint2 = LatLng(56.8, 27.0)
        val apiKey = ""

       // val waypoints=ArrayList<LatLng>()
//        waypoints.add(wayPoint1)
//        waypoints.add(wayPoint2)


//        val startik = LatLng(50.0, 30.0)
//      val endik = LatLng(56.8, 27.0)

        val waypointsString = waypoints.joinToString("|") { "${it.latitude},${it.longitude}" }

        Log.d("555555555555","start = "+origin)
        Log.d("555555555555","destination = "+destination)
        Log.d("555555555555","wayp[oints = "+waypointsString)


        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&waypoints=$waypointsString&key=$apiKey"


        val finalUrl = URL(url)
        val connection = finalUrl.openConnection() as HttpURLConnection
        var inputStream: InputStream? = null
        try {
            inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
            val jsonResponse = stringBuilder.toString()
            Log.d("11111111111111", jsonResponse)

            val jsonObject = JSONObject(jsonResponse)
            val routesArray = jsonObject.getJSONArray("routes")
            val routeObject = routesArray.getJSONObject(0) // Assuming there's only one route


            val overviewPolyline = routeObject.getJSONObject("overview_polyline")
            val polylineString = overviewPolyline.getString("points")

            // Decode polyline string into a list of LatLng points
            val decodedPolyline = PolyUtil.decode(polylineString)


            //
            val legsArray = routeObject.getJSONArray("legs")
            val firstLeg = legsArray.getJSONObject(0) // Assuming there's only one leg
            val startLocation = firstLeg.getJSONObject("start_location")
            val startLat = startLocation.getDouble("lat")
            val startLng = startLocation.getDouble("lng")

            val endLocation = firstLeg.getJSONObject("end_location")
            val endLat = endLocation.getDouble("lat")
            val endLng = endLocation.getDouble("lng")


            // Update UI on the main thread
            activity?.runOnUiThread {
                val polylineOptions = PolylineOptions().addAll(decodedPolyline).color(Color.GRAY)
                mMap.addPolyline(polylineOptions)

                val startPoint = LatLng(startLat, startLng)
                mMap.addMarker(MarkerOptions().position(startPoint).title("Start")
//                    .icon(BitmapDes   criptorFactory.fromResource(R.drawable.k)))
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_start_route_foreground))))

                mMap.addMarker(MarkerOptions().position(destinationLatLng).title("Finish")
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_finish_route_foreground))))

                for ((index, waypoint) in waypoints.withIndex()) {
                    mMap.addMarker(MarkerOptions().position(waypoint).title("Stop ${index + 1}")
                        .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(R.drawable.ic_stop_route_foreground))))
                }


            }

           // Thread.sleep(2000)
            activity?.runOnUiThread {
                val startPoint = LatLng(startLat, startLng)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15f))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
            inputStream?.close()
        }
    }

    private fun resizeBitmap(drawableId: Int): Bitmap {
        val imageBitmap =
            BitmapFactory.decodeResource(resources, drawableId)
        return Bitmap.createScaledBitmap(imageBitmap, 100, 100, false)
    }

    private fun getBitmapFromVectorDrawable(@DrawableRes drawableId: Int): Bitmap {
        var drawable = AppCompatResources.getDrawable(requireActivity().applicationContext, drawableId)
            ?: throw IllegalArgumentException("Drawable not found")
        drawable = DrawableCompat.wrap(drawable).mutate()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

}