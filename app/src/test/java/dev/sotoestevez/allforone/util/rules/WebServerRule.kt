package dev.sotoestevez.allforone.util.rules

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dev.sotoestevez.allforone.api.services.AuthService
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Test rule to use in tests demanding web connections, it mocks a webserver
 * and a Retrofit factory to communicate with it using the application services
 *
 */
class WebServerRule: TestWatcher() {

	/**
	 * Mock of the web server to use in tests
	 */
	val mock: MockWebServer = MockWebServer()

	/**
	 * Retrofit factory using the mock server
	 */
	val factory: Retrofit = Retrofit.Builder()
		.baseUrl(mock.url("/"))
		.addCallAdapterFactory(NetworkResponseAdapterFactory())
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	override fun finished(description: Description) {
		super.finished(description)
		mock.shutdown()
	}


}