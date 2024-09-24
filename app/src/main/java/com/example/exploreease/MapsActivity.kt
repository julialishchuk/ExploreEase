package com.example.exploreease

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.exploreease.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.GoogleMap.OnPoiClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.android.PolyUtil
import com.google.maps.model.GeocodingResult
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Arrays


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mSearchView: SearchView
    private val defaultLocation = Location("default location")
    private var currentLocation : Location = defaultLocation
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private var placesClient: PlacesClient? = null
    private val TAG="11111111111111"


    companion object{
        private const val FINE_PERMISSION_CODE = 1
    }

    init {
        defaultLocation.longitude = 70.5
        defaultLocation.latitude = 35.6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mSearchView = findViewById(R.id.search_view)

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val location = mSearchView.query.toString()
                searchLocationByLocationName(location)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        updateLastLocation()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        Places.initialize(applicationContext, "");
        placesClient = Places.createClient(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(currentLocation.latitude, currentLocation.longitude)//LatLng(50.0, 25.0)
        val markerOptions = MarkerOptions()
            .position(sydney)
            .title("Init location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))



        Log.d("11111111111111","gtrfg")
        Thread {
           getRouteAndDraw()
        }.start()


        mMap.setOnMapClickListener { latLng -> // When the map is clicked, retrieve the coordinates of the clicked position
            Log.i(TAG, "Map is clicked: " )
            getPlaceIdByCoordinates(latLng)
        }

//        mMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
//            Log.d(TAG,"OnMarkerClickListener "+marker)
//            //fetchPlaceDetails(marker.position)
//            false // Return true to indicate that we have consumed the event
//        })

        mMap.setOnPoiClickListener(OnPoiClickListener { poi ->
            Log.i(TAG, "Point of Interest clicked: " + poi.name)
            // You can also use poi.placeId to fetch more details via Google Places API
           getPlaceInfoByPlaceID(poi.placeId)
        })



    }

    fun searchLocationByLocationName(location : String){
        var addressList : List<Address>? = null
        if (location.isNotEmpty()  ){
            val geocoder = Geocoder(this@MapsActivity)
            try {
                addressList = geocoder.getFromLocationName(location, 1)
            }
            catch (e:Exception){

            }
            if (addressList.isNullOrEmpty()) return
            val address = addressList[0]

            val latLong=LatLng(address.latitude, address.longitude)
            mMap.addMarker(MarkerOptions().position(latLong).title(location))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong,10F))
        }
    }

    fun getPlaceIdByCoordinates(latLng: LatLng) {
        val context: GeoApiContext = GeoApiContext.Builder()
            .apiKey("")
            .build()
        try {
            val results: Array<GeocodingResult> =
                GeocodingApi.reverseGeocode(context,  com.google.maps.model.LatLng(latLng.latitude, latLng.longitude)).await()
            if (results != null && results.size > 0) {
                Log.d(TAG,"= "+results[0])
                for ((index,r) in results.withIndex())
                {
                   // Log.d(TAG,"for : "+r)
                    if (index==0) getPlaceInfoByPlaceID(r.placeId, true)
                   // else getPlaceInfoByPlaceID(r.placeId)

                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
       // return null
    }

    private fun getPlaceInfoByPlaceID(placeId : String, showToast: Boolean = false){


        val placeFields: List<Place.Field> =
            Arrays.asList<Place.Field>(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.PRICE_LEVEL, Place.Field.TYPES, Place.Field.OPENING_HOURS, Place.Field.BUSINESS_STATUS)
        //FIX THIS ADD PLACE ID?
        val request = FetchPlaceRequest.newInstance(
            placeId, placeFields
        )
        placesClient!!.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place: Place = response.place
                // Display detailed information about the place
                val placeName: String? = place.getName()
                val placeAddress: String? = place.getAddress()
                val message = "Place Name: $placeName\nPlace Address: $placeAddress"
                if (showToast)Toast.makeText(this@MapsActivity, message, Toast.LENGTH_LONG).show()
                Log.d(TAG, "place = "+place.toString())
            }
            .addOnFailureListener { exception: java.lang.Exception? ->
                Toast.makeText(
                    this@MapsActivity,
                    "Failed to fetch place details",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("PlacesAPI", "Place fetch failure: " + exception);
            }



    }



    private fun updateLastLocation(){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE)
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
               if (it!=null)
               {
                   currentLocation = it
                   val sydney = LatLng(currentLocation.latitude, currentLocation.longitude)//LatLng(50.0, 25.0)
                   val markerOptions = MarkerOptions()
                       .position(sydney)
                       .title("My location")
                       .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                  // markerOptions.snippet("Detailed information here...");
                  // markerOptions.infoWindowAnchor(5000F,6000F);

                   mMap.addMarker(markerOptions)
                   mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

               }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            updateLastLocation()
        }
        else currentLocation = defaultLocation
    }



    private fun getRouteAndDraw() {
        Log.d("11111111111111", "fgr")
        val origin = "Spartak Lviv"
        val destination = "Kyiv"
        val apiKey = ""
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&key=$apiKey"

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


            //
            // Update UI on the main thread
            runOnUiThread {
                val polylineOptions = PolylineOptions().addAll(decodedPolyline)
                mMap.addPolyline(polylineOptions)

                // Move camera to the start position


            }

            Thread.sleep(4000)
            runOnUiThread {
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






}