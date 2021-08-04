package dev.sotoestevez.allforone.api

import android.content.Context
import dev.sotoestevez.allforone.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

class ApiFactory(context: Context) {

	private var service: Retrofit = Retrofit.Builder()
		.baseUrl(context.getString(R.string.server_ip))
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	public fun getAuthService() : AuthService {
		return service.create(AuthService::class.java)
	}

}

data class Credentials(val auth: String)

interface AuthService {

	@POST("/auth/login")
	suspend fun sendCredentials(): Credentials

}