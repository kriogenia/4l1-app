package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class UserRepositoryTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	@Before
	fun beforeEach() {
		mockkObject(ApiFactory)
		every { ApiFactory.getUserService() } returns mockk()
		mockkConstructor(ApiRequest::class)
	}


}