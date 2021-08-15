package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiRequest
import io.mockk.every
import io.mockk.mockkConstructor
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    @Before
    fun beforeEach() {
        mockkConstructor(ApiRequest::class)
        every {  }
    }

    @Test
    fun signIn_validToken


}