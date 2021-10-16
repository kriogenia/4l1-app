package dev.sotoestevez.allforone.util.webserver

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

/**
 * MockWebServer [Dispatcher] mocking all the calls to the /feed endpoint
 */
object FeedDispatcher: Dispatcher() {

	override fun dispatch(request: RecordedRequest): MockResponse {
		return when(request.path) {
			"/feed/messages?page=1" -> MockResponse().setResponseCode(200).setBody(FEED_MESSAGES_EMPTY_200)
			else -> MockResponse().setResponseCode(404)
		}
	}

}