package dev.sotoestevez.allforone.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dev.sotoestevez.allforone.R

class LaunchActivity : AppCompatActivity() {

	private lateinit var googleSignInClient : GoogleSignInClient

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_launch)
		// Configure sign-in to request the user's profile and sign in client
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestEmail()
			.build()
		googleSignInClient = GoogleSignIn.getClient(this, gso)
	}

	override fun onStart() {
		super.onStart()
		// Check for existing Google Sign In account
		val account = GoogleSignIn.getLastSignedInAccount(this);
		if (account != null) {
			startActivity(Intent(this, MainActivity::class.java))
		}
	}

}