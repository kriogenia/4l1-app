package dev.sotoestevez.allforone.ui.activities.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.databinding.ActivityLocationBinding
import dev.sotoestevez.allforone.ui.viewmodel.ExtendedViewModelFactory
import dev.sotoestevez.allforone.ui.view.PrivateActivity
import dev.sotoestevez.allforone.util.extensions.logError
import java.util.*
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import dev.sotoestevez.allforone.util.extensions.logDebug
import dev.sotoestevez.allforone.vo.UserMarker
import dev.sotoestevez.allforone.util.extensions.logWarning
import dev.sotoestevez.allforone.util.extensions.toast
import dev.sotoestevez.allforone.util.helpers.BitmapGenerator

/** Activity with the map and all the logic related to the location sharing */
@SuppressLint("MissingPermission")
class LocationActivity : PrivateActivity(), OnMapReadyCallback {

	override val model: LocationViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityLocationBinding

	private lateinit var map: GoogleMap
	private lateinit var locationProvider: FusedLocationProviderClient
	private var locationCallback: LocationCallback? = null
	private var userControllingMap = false

	companion object {
		private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION  = 1
		private const val DEFAULT_ZOOM = 15F
		private const val INTERVAL = 10000L // 10s
		private const val MIN_INTERVAL = 5000L // 5s
	}

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.KEEPER, User.Role.PATIENT)

	private var mPermissionGranted = false

	override fun bindLayout() {
		binding = ActivityLocationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
	}

	override fun attachObservers() {
		super.attachObservers()
		model.lastKnownLocation.observe(this) { it?.let { updateLocation(it) } }
		model.newUserMarker.observe(this) { it?.let { addMarker(it) }	}
		model.leavingUserMarker.observe(this) { it?.let { removeMarker(it) }}
	}

	@Suppress("KDocMissingDocumentation")
	override fun onDestroy() {
		model.stop()
		map.clear()
		locationCallback?.let { locationProvider.removeLocationUpdates(it) }
		super.onDestroy()
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * Requests the permission to get the user fine location
	 */
	override fun onMapReady(googleMap: GoogleMap) {
		logDebug("Map loaded")
		map = googleMap
		map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
		map.setOnCameraMoveStartedListener { userControllingMap = it == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE }
		locationProvider = LocationServices.getFusedLocationProviderClient(this)

		getLocationPermission()
		getDeviceLocation()
	}

	private fun getLocationPermission() {
		if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			logDebug("Location permission granted")
			mPermissionGranted = true
			setLocationMode()
		} else {
			logDebug("Location permission not granted. Requesting...")
			ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
				PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
		}
	}

	@Suppress("KDocMissingDocumentation")
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String?>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
			logDebug("Location permission obtained")
			mPermissionGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
		}
		setLocationMode()
	}

	/**
	 * Updates the map's UI settings based on whether the user has granted location permission.
	 */
	private fun setLocationMode() {
		try {
			if (mPermissionGranted) {
				map.isMyLocationEnabled = true
				map.uiSettings.isMyLocationButtonEnabled = true
			} else {
				map.isMyLocationEnabled = false
				map.uiSettings.isMyLocationButtonEnabled = false
				getLocationPermission()
			}
		} catch (e: SecurityException) {
			logError("Exception: " + e.message, e)
		}
	}

	/**
	 * Gets the current location of the device, and positions the map's camera.
	 */
	private fun getDeviceLocation() {
		try {
			if (mPermissionGranted) {
				val request = LocationRequest.create().apply {
					priority = LocationRequest.PRIORITY_HIGH_ACCURACY
					interval = INTERVAL
					fastestInterval = MIN_INTERVAL
				}
				locationCallback = object : LocationCallback() {
					override fun onLocationResult(locationResult: LocationResult) {
						model.updateLocation(locationResult.lastLocation)
					}
				}
				locationProvider.requestLocationUpdates(request, locationCallback!!, Looper.getMainLooper())
			}
		} catch (e: SecurityException) {
			logError("Exception: " + e.message, e)
		}
	}

	private fun addMarker(userMarker: UserMarker) {
		logDebug("Adding new user maker")
		if (!this::map.isInitialized) return
		val marker = map.addMarker(userMarker.build().also {
			it.icon(BitmapGenerator.getMarker(this, R.drawable.ic_patient_marker, userMarker.color!!))
		})
		if (marker == null) {
			logWarning("An error has occurred adding a marker to the map")
		} else {
			model.storeMarker(marker.apply { tag = userMarker.id })
			toast(String.format(getString(R.string.user_joined_search), userMarker.displayName))   // Notify the new joining
		}
	}

	private fun updateLocation(location: Location) {
		// TODO show out of bounds marker?
		// TODO save zoom?
		if (userControllingMap) return
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), DEFAULT_ZOOM))
	}

	private fun removeMarker(marker: Marker) {
		logDebug("Removing a marker")
		toast("${marker.title} has left the room")
		marker.remove()
	}

}