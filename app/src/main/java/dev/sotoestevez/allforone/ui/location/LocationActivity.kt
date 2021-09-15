package dev.sotoestevez.allforone.ui.location

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
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityLocationBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.location.LocationViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import dev.sotoestevez.allforone.util.extensions.logError
import java.util.*
import com.google.android.gms.location.LocationResult

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dev.sotoestevez.allforone.ui.keeper.qr.QRScannerActivity
import dev.sotoestevez.allforone.util.extensions.logDebug

/** Activity with the map and all the logic related to the location sharing */
@SuppressLint("MissingPermission")
class LocationActivity : PrivateActivity(), OnMapReadyCallback {

	override val model: LocationViewModel by viewModels { ExtendedViewModelFactory(this) }

	private lateinit var binding: ActivityLocationBinding

	private lateinit var map: GoogleMap
	private lateinit var locationProvider: FusedLocationProviderClient

	companion object {
		private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION  = 1
		private const val DEFAULT_PADDING = 20
		private const val MIN_DISPLACEMENT = 10F
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
		model.lastKnownLocation.observe(this) { it?.let { updateOwnLocation(it) } }
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * Requests the permission to get the user fine location
	 */
	override fun onMapReady(googleMap: GoogleMap) {
		map = googleMap
		locationProvider = LocationServices.getFusedLocationProviderClient(this)

		getLocationPermission()
		getDeviceLocation()
	}

	private fun getLocationPermission() {
		if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			mPermissionGranted = true
			updateLocationUI()
		} else {
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
			mPermissionGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
		}
		updateLocationUI()
	}

	/**
	 * Updates the map's UI settings based on whether the user has granted location permission.
	 */
	private fun updateLocationUI() {
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
					smallestDisplacement = MIN_DISPLACEMENT
					interval = INTERVAL
					fastestInterval = MIN_INTERVAL
				}
				locationProvider.requestLocationUpdates(request, object : LocationCallback() {
					override fun onLocationResult(locationResult: LocationResult) {
						model.updateLocation(locationResult.lastLocation)
					}
				}, Looper.getMainLooper())
			}
		} catch (e: SecurityException) {
			logError("Exception: " + e.message, e)
		}
	}

	private fun updateOwnLocation(location: Location) {
		val bounds = LatLngBounds.builder().apply { include(LatLng(location.latitude, location.longitude)) }
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), DEFAULT_PADDING))
	}

}