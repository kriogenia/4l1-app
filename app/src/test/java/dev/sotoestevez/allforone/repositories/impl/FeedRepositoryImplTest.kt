package dev.sotoestevez.allforone.repositories.impl

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.BondGenerateResponse
import dev.sotoestevez.allforone.api.schemas.FeedMessageResponse
import dev.sotoestevez.allforone.api.schemas.PlainMessage
import dev.sotoestevez.allforone.api.services.FeedService
import dev.sotoestevez.allforone.api.services.UserService
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkConstructor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeedRepositoryImplTest {

    @get:Rule
    var coroutineRule: CoroutineRule = CoroutineRule()

    // Mocks
    private lateinit var mockFeedService: FeedService

    // Test object
    private lateinit var repo: FeedRepositoryImpl

    @Before
    fun beforeEach() {
        mockFeedService = mockk()
        mockkConstructor(ApiRequest::class)
        repo = FeedRepositoryImpl(mockFeedService)
    }

    @Test
    fun `should return the correct list of messages`(): Unit = coroutineRule.testDispatcher.runBlockingTest {
        val msg1 = Message("1","message1", User(id = "id1", displayName = "name1"), Message.Type.TEXT)
        val msg2 = Message("2","message2", User(id = "id2", displayName = "name2"), Message.Type.TEXT)

        val feedMessageResponse = FeedMessageResponse(arrayOf(
            PlainMessage(msg1.id, msg1.message, msg1.user.id!!, msg1.user.displayName!!, msg1.timestamp, msg1.type),
            PlainMessage(msg2.id, msg2.message, msg2.user.id!!, msg2.user.displayName!!, msg2.timestamp, msg2.type)
        ))
        val response: NetworkResponse.Success<FeedMessageResponse> = mockk()
        coEvery { response.code } returns 200
        coEvery { response.body } returns feedMessageResponse
        coEvery { mockFeedService.messages(any(), any()) } returns response

        val result = repo.getMessages(1, "token")

        coVerify(exactly = 1) { mockFeedService.messages("token", 1) }
        Assert.assertEquals(result.size, 2)
        Assert.assertEquals(result[0], msg1)
        Assert.assertEquals(result[1], msg2)    // Most recent first
    }

}