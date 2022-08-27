package com.example.campusinteligenteiot.ui.home.car.details

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.trip.TripResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.CarDriverFragmentBinding
import com.example.campusinteligenteiot.databinding.CarPassengerFragmentBinding
import com.example.campusinteligenteiot.databinding.TripDetailsFragmentBinding
import com.example.campusinteligenteiot.ui.drawer.friends.UsersAdapter
import com.example.campusinteligenteiot.ui.home.car.adapter.TripAdapter
import com.example.campusinteligenteiot.ui.home.car.driver.CarDriverViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.add_trip_fragment.*
import kotlinx.android.synthetic.main.add_trip_fragment.mapView
import kotlinx.android.synthetic.main.fragment_main_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TripDetailsFragment : Fragment(), OnMapReadyCallback, MapboxMap.OnMapClickListener {
    private val viewModel by viewModels<TripDetailsViewModel>()
    private var _binding: TripDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentUser: UsersResponse
    private lateinit var driver: UsersResponse
    private lateinit var adapter: UsersAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var trip: TripResponse
    private var tripId: String? = null
    private val REQUEST_CODE_AUTOCOMPLETE = 7171
    private var mapboxMap: MapboxMap? = null
    private var locationComponent: LocationComponent? = null
    private var currentRoute: DirectionsRoute? = null
    private var navigationMapRoute: NavigationMapRoute? = null
    private val TAG = "DirectionsActivity"
    private val geoJsonSourceLayerId = "GeoJsonSourceLayerId"
    private val symbolIconId = "SymbolIconId"
    private var firstPoint: Point? = null
    private var secondPoint: Point? = null
    private var source: GeoJsonSource? = null
    private var passengersList = ArrayList<UsersResponse>()
    var permsRequestCode = 100
    var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), getString(R.string.access_token))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TripDetailsFragmentBinding.inflate(inflater,container,false)

        tripId = arguments?.getString("tripId")
        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        currentUser = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            trip = viewModel.getSingleTrip(tripId!!)
            setPointsAndRoute(trip)
            setTripData(trip)
            for (id in trip.passengers) {
                passengersList.add(viewModel.getUser(id))
            }
            adapter.setAssistantsList(passengersList)
            adapter.notifyItemInserted(passengersList.size - 1)
        }

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.customScrollView.isEnableScrolling = true
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.getMapAsync(this)

        initUsersRecyclerView(view)

        binding.backButton.setOnClickListener{
            if(trip.driver.equals(currentUser.id)){
                findNavController().navigate(R.id.action_tripDetailsFragment_to_carDriverFragment)
            }
            else{
                findNavController().navigate(R.id.action_tripDetailsFragment_to_carPassengerFragment)
            }
        }

        binding.mapView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> customScrollView.isEnableScrolling = false
                    MotionEvent.ACTION_UP -> customScrollView.isEnableScrolling = true
                    MotionEvent.ACTION_MOVE -> customScrollView.isEnableScrolling = false
                    MotionEvent.ACTION_CANCEL -> customScrollView.isEnableScrolling = true//Do Something
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        binding.customScrollView.setOnScrollChangeListener { v: View, scrollX: Int, scrollY: Int, _: Int, _: Int ->
            binding.swipeRefresh.isEnabled = scrollY == 0
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            GlobalScope.launch(Dispatchers.Main){
                trip = viewModel.getSingleTrip(tripId!!)
                val passengersList = ArrayList<UsersResponse>()
                for (id in trip.passengers) {
                    passengersList.add(viewModel.getUser(id))
                }
                adapter.setAssistantsList(passengersList)
                adapter.notifyDataSetChanged()
            }

        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(getString(R.string.map_style)) {
                style: Style? ->
            enableLocationComponent(style)
            addDestinationIconSymbolLayer(style)
            mapboxMap.addOnMapClickListener(this)

            setUpSource(style!!)

            setUpLayer(style!!)

            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_location_on_red_24dp, null)
            val bitmapUtils = BitmapUtils.getBitmapFromDrawable(drawable)
            style.addImage(symbolIconId, bitmapUtils!!)
        }
    }

    private fun setPointsAndRoute(trip: TripResponse) {
        firstPoint = Point.fromLngLat(trip.originPoint[1], trip.originPoint[0])
        secondPoint = Point.fromLngLat(trip.finalPoint[1], trip.finalPoint[0])
        source = mapboxMap!!.style!!.getSourceAs<GeoJsonSource>("destination-source-id")
        source?.setGeoJson(Feature.fromGeometry(firstPoint))
        source?.setGeoJson(Feature.fromGeometry(secondPoint))
        getRoute(firstPoint!!, secondPoint!!)
    }

    override fun onMapClick(point: LatLng): Boolean {
        return true
    }

    private fun getRoute(origin: Point, destination: Point) {
        NavigationRoute.builder(context).accessToken(Mapbox.getAccessToken()!!).origin(origin)
            .destination(destination)
            .build()
            .getRoute(object : Callback<DirectionsResponse> {
                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    // You can get the generic HTTP info about the response
                    Log.d(TAG, "Response code: " + response.body())
                    if (response.body() == null) {
                        Log.d(TAG, "No routes found, make sure you set the right user and access token")
                        return
                    }
                    else if (response.body()!!.routes().size < 1){
                        Log.e(TAG, "No routes found")
                        return
                    }
                    currentRoute = response.body()!!.routes()[0]

                    //Draw the route on the map
                    if (navigationMapRoute != null) {
                        navigationMapRoute!!.removeRoute()
                    }
                    else {
                        navigationMapRoute = NavigationMapRoute(null, mapView, mapboxMap!!, R.style.NavigationMapRoute)
                    }
                    navigationMapRoute!!.addRoute(currentRoute)
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    Log.e(TAG, "Error: " + t.message)
                }

            })
    }

    private fun setUpSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(geoJsonSourceLayerId))
    }

    private fun setUpLayer(loadedMapStyle: Style) {
        loadedMapStyle.addLayer(SymbolLayer("SYMBOL_LAYER_ID", geoJsonSourceLayerId).withProperties(
            PropertyFactory.iconImage(symbolIconId),
            PropertyFactory.iconOffset(arrayOf(0f, -8f))
        ))
    }

    private fun addDestinationIconSymbolLayer(loadedMapStyle: Style?) {
        loadedMapStyle!!.addImage("destination-icon-id", BitmapFactory.decodeResource(this.resources, R.drawable.mapbox_marker_icon_default))

        val geoJsonSource = GeoJsonSource("destination-source-id")
        loadedMapStyle.addSource(geoJsonSource)
        val destinationSymbolLayer = SymbolLayer("destination-symbol-layer-id", "destination-source-id")
        destinationSymbolLayer.withProperties(
            PropertyFactory.iconImage("destination-icon-id"),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconIgnorePlacement(true))

        loadedMapStyle.addLayer(destinationSymbolLayer)
    }

    private fun enableLocationComponent(loadedMapStyle: Style?) {
        //Check if permissions are enabled and if not request
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED){
            requestMapPermissions()
        }
        else{
            locationComponent = mapboxMap!!.locationComponent
            locationComponent!!.activateLocationComponent(requireContext(), loadedMapStyle!!)
            locationComponent!!.setLocationComponentEnabled(true)
            //Set the component's camera mode
            locationComponent!!.setCameraMode(CameraMode.TRACKING)
        }
    }

    private fun requestMapPermissions() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[0])
            || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),permissions[1])
            || ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),permissions[2])){
            Toast.makeText(requireContext(), "Permisos rechazados", Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),permissions,permsRequestCode)
        }
    }

    private fun setTripData(trip: TripResponse) {
        GlobalScope.launch(Dispatchers.Main){
            driver = viewModel.getUser(trip.driver)
            binding.titleFragment.text = getString(R.string.trip_of) + " ${driver.userName}"
            val storageReference = FirebaseStorage.getInstance()
            val gsReference = storageReference.getReferenceFromUrl(driver.profileImage)
            gsReference.downloadUrl.addOnSuccessListener {
                Glide.with(requireContext()).load(it).into(binding.userImage)
            }
        }
        binding.dateTrip.text = getString(R.string.date_trip) + " ${toStringWithTimeText(trip.departureDate)}"
    }

    fun toStringWithTime(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(this)
    }

    fun toStringWithTimeText(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd-MM-yyyy HH:mm").format(this)
    }


    private fun initUsersRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.passengersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UsersAdapter(currentUser, requireContext(), false)
        adapter.setAssistantsList(passengersList)
        recyclerView.adapter = adapter

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == permsRequestCode){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED){
                enableLocationComponent(mapboxMap!!.style)
            }
            else{
                Toast.makeText(requireContext(), "Permisos rechazados por primera vez", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}