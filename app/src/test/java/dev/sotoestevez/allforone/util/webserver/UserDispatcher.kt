package dev.sotoestevez.allforone.util.webserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object UserDispatcher: Dispatcher() {

	override fun dispatch(request: RecordedRequest): MockResponse {
		return when(request.path) {
			"/user/bond/establish"  ->  if (request.headers["Authorization"].equals("valid"))
											MockResponse().setResponseCode(200).setBody(USER_BOND_ESTABLISH_200)
										else MockResponse().setResponseCode(401).setBody(USER_BOND_ESTABLISH_401)
			"/user/bond/generate"   ->  MockResponse().setResponseCode(200).setBody(USER_BOND_GENERATE_200)
			"/user/cared"           ->   if (request.headers["Authorization"].equals("defined"))
											MockResponse().setResponseCode(200).setBody(USER_CARED_DEFINED_200)
										else MockResponse().setResponseCode(200).setBody(USER_CARED_UNDEFINED_200)
			"/user/update"          ->  if (request.headers["Authorization"].equals("valid"))
											MockResponse().setResponseCode(201).setBody(USER_UPDATE_201)
										else MockResponse().setResponseCode(401).setBody(USER_UPDATE_401)
			else -> MockResponse().setResponseCode(404)
		}
	}

}