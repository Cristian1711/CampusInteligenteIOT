package com.example.campusinteligenteiot.ui.home.car.driver.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.AddTripFragmentBinding
import com.example.campusinteligenteiot.databinding.CarDriverFragmentBinding
import com.example.campusinteligenteiot.ui.home.car.driver.CarDriverViewModel
import android.widget.TimePicker

import android.app.TimePickerDialog

import android.widget.DatePicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import kotlinx.android.synthetic.main.fragment_main_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.api.model.trip.TripCall
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.add_trip_fragment.*
import kotlinx.android.synthetic.main.fragment_main_home.mapView
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class AddTripFragment : Fragment(), OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private val viewModel by viewModels<AddTripViewModel>()
    private var _binding: AddTripFragmentBinding? = null
    private val binding get() = _binding!!
    var date: Calendar? = null
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
    private lateinit var user: UsersResponse
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
        _binding = AddTripFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.customScrollView.isEnableScrolling = true
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.getMapAsync(this)

        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(user.profileImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.userImage)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_addTripFragment_to_carDriverFragment)
        }

        binding.etDate.setOnClickListener {
            showDateTimePicker()
        }

        binding.buttonResetPoints.setOnClickListener {
            if(firstPoint != null){
                navigationMapRoute!!.removeRoute()
                firstPoint = null
            }
        }

        binding.buttonSave.setOnClickListener {
            if(firstPoint == null || secondPoint == null){
                val builder = AlertDialog.Builder(requireContext())
                val myView = layoutInflater.inflate(R.layout.generic_dialog_1_button, null)
                builder.setView(myView)
                val dialog = builder.create()

                myView.Question.text = getString(R.string.must_have_points)
                myView.Question2.text = getString(R.string.touch_map)

                dialog.show()

                myView.cancelButton.setOnClickListener{
                    dialog.cancel()
                }
            }
            else{
                if(date == null){
                    val builder = AlertDialog.Builder(requireContext())
                    val myView = layoutInflater.inflate(R.layout.generic_dialog_1_button, null)
                    builder.setView(myView)
                    val dialog = builder.create()

                    myView.Question.text = getString(R.string.no_date)
                    myView.Question2.text = getString(R.string.using_calendar)

                    dialog.show()

                    myView.cancelButton.setOnClickListener{
                        dialog.cancel()
                    }

                }
                else{
                    GlobalScope.launch(Dispatchers.Main) {
                        val tripCall = TripCall(
                            arrayListOf(secondPoint!!.latitude(), secondPoint!!.longitude()),
                            arrayListOf(firstPoint!!.latitude(), firstPoint!!.longitude()),
                            arrayListOf(user.id),
                            toStringWithTime(date!!.time),
                            binding.textSeats.text.toString().toInt(),
                            false,
                            user.id,
                            true
                        )

                        viewModel.addNewTrip("null", tripCall)
                        binding.progressBar.visibility = VISIBLE
                        Handler().postDelayed({
                            findNavController().navigate(R.id.action_addTripFragment_to_carDriverFragment)
                        }, 1500)
                    }
                }
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
    }

    fun toStringWithTime(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(this)
    }

    fun toStringWithTimeText(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd-MM-yyyy HH:mm").format(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            /** Retrieve selected location's CarmenFeature **/
            val selectedCarmenFeature = PlaceAutocomplete.getPlace(data)

            /** Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above.
             * Then retrieve and update the source designated for showing a selected location's symbol layer icon **/
            if (mapboxMap != null) {
                val style = mapboxMap!!.style
                if (style != null) {
                    val source = style.getSourceAs<GeoJsonSource>(geoJsonSourceLayerId)
                    source?.setGeoJson(FeatureCollection.fromFeatures(arrayOf(Feature.fromJson(selectedCarmenFeature.toJson()))))

                    /** Move map camera to the selected location **/
                    mapboxMap!!.animateCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder()
                        .target(LatLng((selectedCarmenFeature.geometry() as Point?)!!.latitude(),
                            (selectedCarmenFeature.geometry() as Point?)!!.longitude()))
                        .zoom(14.0)
                        .build()), 4000)
                }
            }
        }
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

    private fun showDateTimePicker() {
        val currentDate: Calendar = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                date?.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(context,
                    { view, hourOfDay, minute ->
                        date?.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date?.set(Calendar.MINUTE, minute)
                        binding.etDate.setText(toStringWithTimeText(date!!.time))
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        ).show()
    }

    override fun onMapClick(point: LatLng): Boolean {
        if(firstPoint == null){
            firstPoint = Point.fromLngLat(point.longitude, point.latitude)
            source = mapboxMap!!.style!!.getSourceAs<GeoJsonSource>("destination-source-id")
            source?.setGeoJson(Feature.fromGeometry(firstPoint))
        }
        else{
            secondPoint = Point.fromLngLat(point.longitude, point.latitude)
            source = mapboxMap!!.style!!.getSourceAs<GeoJsonSource>("destination-source-id")
            source?.setGeoJson(Feature.fromGeometry(secondPoint))
            getRoute(firstPoint!!, secondPoint!!)
        }
        //val destinationPoint = Point.fromLngLat(point.longitude, point.latitude)
        //val originPoint = Point.fromLngLat(locationComponent!!.lastKnownLocation!!.longitude, locationComponent!!.lastKnownLocation!!.latitude)

        //val source = mapboxMap!!.style!!.getSourceAs<GeoJsonSource>("destination-source-id")
        //source?.setGeoJson(Feature.fromGeometry(destinationPoint))
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