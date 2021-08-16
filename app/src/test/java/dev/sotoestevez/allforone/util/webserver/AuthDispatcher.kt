package dev.sotoestevez.allforone.util.webserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object AuthDispatcher: Dispatcher() {

	override fun dispatch(request: RecordedRequest): MockResponse {
		return when(request.path) {
			"/auth/signin/valid" -> MockResponse().setResponseCode(200).setBody(AUTH_SIGNIN_200)
			"/auth/signin/" -> MockResponse().setResponseCode(400).setBody(AUTH_SIGNIN_400)
			"/auth/signin/invalid" -> MockResponse().setResponseCode(400).setBody(AUTH_SIGNIN_400)
			else -> MockResponse().setResponseCode(404)
		}
	}

}