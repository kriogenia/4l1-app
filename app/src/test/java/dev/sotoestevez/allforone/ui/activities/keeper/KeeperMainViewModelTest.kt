package dev.sotoestevez.allforone.ui.activities.keeper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.util.helpers.SessionManager
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.NullPointerException

@ExperimentalCoroutinesApi
class KeeperMainViewModelTest {

	@get:Rule
	var coroutineRule: CoroutineRule = CoroutineRule()

	@get:Rule
	var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

	// Mocks
	private lateinit var mockUserRepository: UserRepository

	// Test object
	private lateinit var model: KeeperMainViewModel
	private val code = "qrcode"
	private val user = User("id", User.Role.BLANK)

	@Before
	fun beforeEach() {
		// Mocks
		mockUserRepository = mockk(relaxed = true)
		coEvery { mockUserRepository.getCared(any(), any()) } returns null
		mockkConstructor(SessionManager::class)
		every { anyConstructed<SessionManager>().getAuthToken() } returns "token"
		// Init test object
		model = KeeperMainViewModel(
			mockk(relaxed = true),
			coroutineRule.testDispatcherProvider,
			mockk(),
			mockUserRepository,
			mockk(),
			mockk())
		model.injectUser(user)
	}

	@Test
	fun `should set the cared when the bonding is successful`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		val cared = User("cared", User.Role.PATIENT)
		coEvery { mockUserRepository.getCared(any(), any()) } returns cared

		model.bond(code)

		coVerify(exactly = 1) { mockUserRepository.sendBondingCode(code, "Bearer token") }
		coVerify(exactly = 1) { mockUserRepository.getCared(user,"Bearer token") }
		assertEquals(cared, model.cared.value)
	}

	@Test
	fun `should thrown an error when the cared user recovered is null`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
		coEvery { mockUserRepository.getCared(any(), any()) } returns null
		try {
			model.bond(code)
		} catch (error: NullPointerException) {
			assertEquals("Unable to forge the bond, please try again", error.message)
		} finally {
			coVerify(exactly = 1) { mockUserRepository.sendBondingCode(code, "Bearer token") }
			coVerify(exactly = 1) { mockUserRepository.getCared(user, "Bearer token") }
			assertNull(model.cared.value)
		}
	}


}