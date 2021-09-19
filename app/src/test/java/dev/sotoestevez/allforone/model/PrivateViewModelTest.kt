package dev.sotoestevez.allforone.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.sotoestevez.allforone.api.schemas.RefreshResponse
import dev.sotoestevez.allforone.data.Session
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class PrivateViewModelTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	@get:Rule
	var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

	// Mocks
	private lateinit var mockSessionRepo: SessionRepository

	// Test object
	private lateinit var model: PrivateViewModel

	@Before
	fun beforeEach() {
		// Mocks
		mockSessionRepo = mockk()
		mockkConstructor(SessionManager::class)
		every { anyConstructed<SessionManager>().getAuthToken() } returns "token"
		// Init test object
		model = object: PrivateViewModel(mockk(relaxed = true), coroutineRule.testDispatcherProvider, mockSessionRepo) {}
		model.injectUser(User("id", "google", User.Role.BLANK))
	}

	@After
	fun afterEach() {
		unmockkAll()            // reset all the mocks
	}

	@Test
	fun `authToken() should give the token stored in the SessionManager when present`() {
		coroutineRule.testDispatcher.runBlockingTest {
			assertEquals("Bearer token", model.authHeader())
			coVerify(exactly = 0) { mockSessionRepo.refreshSession(any()) }
		}
	}

	@Test
	fun `authToken() should refresh the session and give a new token if the stored is invalid`() {
		every { anyConstructed<SessionManager>().getAuthToken() } returns null
		every { anyConstructed<SessionManager>().getSession() } returns mockk()

		val session = Session("new", "refresh", 0)
		coEvery { mockSessionRepo.refreshSession(any()) } returns session

		coroutineRule.testDispatcher.runBlockingTest {
			assertEquals("Bearer new", model.authHeader())
			coVerify(exactly = 1) { mockSessionRepo.refreshSession(any()) }
			verify(exactly = 1) { anyConstructed<SessionManager>().setSession(session) }
		}
	}


}