package dev.sotoestevez.allforone.model.setup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.sotoestevez.allforone.data.Address
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.entities.SessionManager
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.ui.patient.PatientMainActivity
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SetUpViewModelTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	@get:Rule
	var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

	// Mocks
	private lateinit var mockUserRepo: UserRepository

	// Test object
	private lateinit var model: SetUpViewModel

	@Before
	fun beforeEach() {
		// Mocks
		mockUserRepo = mockk()
		mockkConstructor(SessionManager::class)
		every { anyConstructed<SessionManager>().getAuthToken() } returns "token"
		// Init test object
		model = SetUpViewModel(mockk(relaxed = true), coroutineRule.testDispatcherProvider, mockk(), mockUserRepo)
		model.injectUser(User("id", "google", User.Role.BLANK))
	}

	@After
	fun afterEach() {
		unmockkAll()            // reset all the mocks
	}

	@Test
	fun `should replace the name of the user with setDisplayName`() {
		assertEquals("", model.user.value?.displayName)
		model.setDisplayName("name")
		assertEquals("name", model.user.value?.displayName)
	}

	@Test
	fun `should replace the role of the user with setRole`() {
		assertEquals(User.Role.BLANK, model.user.value?.role)
		model.setRole(User.Role.PATIENT)
		assertEquals(User.Role.PATIENT, model.user.value?.role)
	}

	@Test
	fun `should replace the main phone number of the user with setMainPhoneNumber`() {
		assertEquals("", model.user.value?.mainPhoneNumber)
		model.setMainPhoneNumber("123")
		assertEquals("123", model.user.value?.mainPhoneNumber)
	}

	@Test
	fun `should replace the alternative phone number of the user with setAltPhoneNumber`() {
		assertEquals("", model.user.value?.altPhoneNumber)
		model.setAltPhoneNumber("123")
		assertEquals("123", model.user.value?.altPhoneNumber)
	}

	@Test
	fun `should replace the email of the user with setEmail`() {
		assertEquals("", model.user.value?.email)
		model.setEmail("a@a.com")
		assertEquals("a@a.com", model.user.value?.email)
	}

	@Test
	fun `should replace the address of the user with setEmail`() {
		assertEquals(null, model.user.value?.address)
		model.setAddress("street", "door", "locality", "region")
		assertEquals(Address("street", "door", "locality", "region"), model.user.value?.address)
	}

	@Test
	fun `should send the update to the server and change the destiny when invoked sendUpdate`(): Unit =
		coroutineRule.testDispatcher.runBlockingTest {
			model.injectUser(User("id", "googleId", User.Role.PATIENT))
			coEvery { mockUserRepo.update(any(), any()) } returns Unit

			model.sendUpdate()

			coVerify(exactly = 1) { mockUserRepo.update(any(), any()) }
			assertEquals(PatientMainActivity::class.java, model.destiny.value)
		}

}