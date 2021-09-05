package dev.sotoestevez.allforone.ui.location

import androidx.activity.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.databinding.ActivityLocationBinding
import dev.sotoestevez.allforone.model.ExtendedViewModelFactory
import dev.sotoestevez.allforone.model.location.LocationViewModel
import dev.sotoestevez.allforone.ui.PrivateActivity
import java.util.*

class LocationActivity : PrivateActivity(), OnMapReadyCallback {

	private lateinit var mMap: GoogleMap
	private lateinit var binding: ActivityLocationBinding

	override val model: LocationViewModel by viewModels { ExtendedViewModelFactory(this) }

	override val roles: EnumSet<User.Role> = EnumSet.of(User.Role.KEEPER, User.Role.PATIENT)

	override fun bindLayout() {
		binding = ActivityLocationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		val mapFragment = supportFragmentManager
			.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
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
		val sydney = LatLng(-34.0, 151.0)
		mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
		mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
	}
}