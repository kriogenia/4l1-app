package dev.sotoestevez.allforone.api

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.api.services.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory of the different services of the API
 */
object ApiFactory {

	private var service: Retrofit = Retrofit.Builder()
		.baseUrl("http://10.0.2.2:3000")
		.addCallAdapterFactory(NetworkResponseAdapterFactory())
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	/**
	 * @return  service of the /auth endpoints
	 */
	public fun getAuthService() : AuthService {
		return service.create(AuthService::class.java)
	}

}
