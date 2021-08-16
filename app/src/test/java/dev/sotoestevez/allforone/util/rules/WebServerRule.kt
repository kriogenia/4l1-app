package dev.sotoestevez.allforone.util.rules

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import dev.sotoestevez.allforone.api.services.AuthService
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebServerRule: TestWatcher() {

	val mock = MockWebServer()

	val api = Retrofit.Builder()
		.baseUrl(mock.url("/"))
		.addCallAdapterFactory(NetworkResponseAdapterFactory())
		.addConverterFactory(GsonConverterFactory.create())
		.build()

	override fun finished(description: Description) {
		super.finished(description)
		mock.shutdown()
	}


}