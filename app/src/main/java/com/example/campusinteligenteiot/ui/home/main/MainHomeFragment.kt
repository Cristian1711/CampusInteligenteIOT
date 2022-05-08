package com.example.campusinteligenteiot.ui.home.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
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
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.UserProvider
import com.example.campusinteligenteiot.api.model.UsersResponse
import com.example.campusinteligenteiot.databinding.FragmentMainHomeBinding
import com.example.campusinteligenteiot.model.User
import com.example.campusinteligenteiot.ui.authentication.registerdata.RegisterDescriptionViewModel
import com.example.campusinteligenteiot.ui.drawer.chats.ChannelsViewModel
import com.google.firebase.storage.FirebaseStorage
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.image
import io.getstream.chat.android.client.models.name
import kotlinx.android.synthetic.main.fragment_main_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainHomeFragment : Fragment(), OnMapReadyCallback, MapboxMap.OnMapClickListener{
    private val REQUEST_CODE_AUTOCOMPLETE = 7171
    private var mapboxMap: MapboxMap? = null
    private var permissionsManager: PermissionsManager? = null
    private var locationComponent: LocationComponent? = null
    private var currentRoute: DirectionsRoute? = null
    private var navigationMapRoute: NavigationMapRoute? = null
    private val TAG = "DirectionsActivity"
    private val geoJsonSourceLayerId = "GeoJsonSourceLayerId"
    private val symbolIconId = "SymbolIconId"
    private val client = ChatClient.instance()
    private lateinit var user : UsersResponse
    private val viewModel by viewModels<MainHomeViewModel>()

    var permsRequestCode = 100
    var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE)

    private  var _binding: FragmentMainHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(requireContext(), getString(R.string.access_token))
        viewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainHomeBinding.inflate(inflater,container,false)
        viewModel.resultUsers.observe(viewLifecycleOwner, Observer {
            println("ESTOY EN EL FRAGMENTO")
            UserProvider.users = viewModel.resultUsers.value!!
            println("Lista de usuarios")
            println(UserProvider.users)
            GlobalScope.launch(Dispatchers.Main) {
                user = viewModel.getUserFromLocal(viewModel.getId())
                println("DATOS USUARIO DE BASE DE DATOS LOCAL")
                println(user.name)
                println(user.collegeDegree)
                println(user.surname)
                loadProfileImage(user)
                createUserGetStream(user)
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            showProfileImage()
        }, 2000)

        return binding.root
    }

    private fun showProfileImage() {
        binding.profileImageLoading.isVisible = false
        binding.profileImage.isVisible = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(getString(R.string.map_style)) {
                style: Style? ->
            enableLocationComponent(style)
            addDestinationIconSymbolLayer(style)
            mapboxMap.addOnMapClickListener(this)

            btnStart.setOnClickListener { v: View? ->
                val simulateRoute = false
                val options = NavigationLauncherOptions.builder()
                    .directionsRoute(currentRoute)
                    .shouldSimulateRoute(simulateRoute)
                    .build()

                // Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(activity, options)
            }

            initSearchFab()

            setUpSource(style!!)

            setUpLayer(style!!)

            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_location_on_red_24dp, null)
            val bitmapUtils = BitmapUtils.getBitmapFromDrawable(drawable)
            style!!.addImage(symbolIconId, bitmapUtils!!)
        }
    }

    private fun setUpLayer(loadedMapStyle: Style) {
        loadedMapStyle.addLayer(SymbolLayer("SYMBOL_LAYER_ID", geoJsonSourceLayerId).withProperties(
            PropertyFactory.iconImage(symbolIconId),
            PropertyFactory.iconOffset(arrayOf(0f, -8f))
        ))
    }

    private fun setUpSource(loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(geoJsonSourceLayerId))
    }

    private fun initSearchFab() {
        fab_location_search.setOnClickListener{v: View? ->
            val intent = PlaceAutocomplete.IntentBuilder()
                .accessToken(
                    (if (Mapbox.getAccessToken() != null) Mapbox.getAccessToken() else getString(R.string.access_token))!!
                ).placeOptions(PlaceOptions.builder()
                    .backgroundColor(Color.parseColor("#121212"))
                    .limit(10)
                    .build(PlaceOptions.MODE_CARDS))
                .build(activity)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
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
                    mapboxMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
                        .target(LatLng((selectedCarmenFeature.geometry() as Point?)!!.latitude(),
                            (selectedCarmenFeature.geometry() as Point?)!!.longitude()))
                        .zoom(14.0)
                        .build()), 4000)
                }
            }
        }
    }

    private fun addDestinationIconSymbolLayer(loadedMapStyle: Style?) {
        loadedMapStyle!!.addImage("destination-icon-id", BitmapFactory.decodeResource(this.resources, R.drawable.mapbox_marker_icon_default))

        val geoJsonSource = GeoJsonSource("destination-source-id")
        loadedMapStyle.addSource(geoJsonSource)
        val destinationSymbolLayer = SymbolLayer("destination-symbol-layer-id", "destination-source-id")
        destinationSymbolLayer.withProperties(PropertyFactory.iconImage("destination-icon-id"),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconIgnorePlacement(true))

        loadedMapStyle.addLayer(destinationSymbolLayer)
    }

    override fun onMapClick(point: LatLng): Boolean {
        val destinationPoint = Point.fromLngLat(point.longitude, point.latitude)
        val originPoint = Point.fromLngLat(locationComponent!!.lastKnownLocation!!.longitude, locationComponent!!.lastKnownLocation!!.latitude)

        val source = mapboxMap!!.style!!.getSourceAs<GeoJsonSource>("destination-source-id")
        source?.setGeoJson(Feature.fromGeometry(destinationPoint))

        getRoute(originPoint, destinationPoint)
        btnStart!!.isEnabled = true
        btnStart!!.setBackgroundResource(R.drawable.button_1)
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

    private fun createUserGetStream(firebaseUser: UsersResponse){
        val user = io.getstream.chat.android.client.models.User(id = firebaseUser.userName!!).apply {
            name = firebaseUser.userName
            image = firebaseUser.profileImage!!
        }
        val token = client.devToken(user.id)
        println("id = ${firebaseUser.name}\n token = $token")

        client.connectUser(
            user = user,
            token = token
        ).enqueue() { result ->
            if (result.isSuccess) {
                val user: io.getstream.chat.android.client.models.User = result.data().user
                val connectionId: String = result.data().connectionId
            } else {
                println("NO bro")
            }
        }
    }

    private fun loadProfileImage(user: UsersResponse) {
        val media = user.profileImage
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(media!!)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.profileImage)
        }
    }


}