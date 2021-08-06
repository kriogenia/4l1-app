package dev.sotoestevez.allforone.api

import android.content.Context
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.api.services.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory of the different services of the API
 *
 * @constructor
 * Creates an ApiFactory with a Retrofit service to build the services
 *
 * @param context   current context to retrieve the URL from the resources
 */
class ApiFactory(context: Context) {

	private var service: Retrofit = Retrofit.Builder()
		.baseUrl(context.getString(R.string.server_ip))
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	/**
	 * @return  service of the /auth endpoints
	 */
	public fun getAuthService() : AuthService {
		return service.create(AuthService::class.java)
	}

}
