package dev.sotoestevez.allforone.util.webserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object UserDispatcher: Dispatcher() {

	override fun dispatch(request: RecordedRequest): MockResponse {
		return when(request.path) {
			"/user/update" ->   if (request.headers["Authorization"].equals("valid"))
										MockResponse().setResponseCode(201).setBody(USER_UPDATE_201)
								else MockResponse().setResponseCode(401).setBody(USER_UPDATE_401)
			else -> MockResponse().setResponseCode(404)
		}
	}

}