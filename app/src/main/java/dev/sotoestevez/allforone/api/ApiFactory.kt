package dev.sotoestevez.allforone.api

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dev.sotoestevez.allforone.BuildConfig
import dev.sotoestevez.allforone.api.services.AuthService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Factory of the different services of the API
 */
object ApiFactory {

	private var service: Retrofit = Retrofit.Builder()
		.baseUrl(BuildConfig.SERVER_IP)
		.addCallAdapterFactory(NetworkResponseAdapterFactory())
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	/**
	 * @return  service of the /auth endpoints
	 */
	public fun getAuthService(): AuthService {
		return service.create(AuthService::class.java)
	}

}
