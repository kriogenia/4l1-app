package dev.sotoestevez.allforone.api

import android.content.Context
import android.content.res.Resources
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

interface AuthService {

	@POST("/auth/login")
	suspend fun sendCredentials(): String

}