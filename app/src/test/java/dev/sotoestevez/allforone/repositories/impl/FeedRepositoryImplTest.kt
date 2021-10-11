package dev.sotoestevez.allforone.repositories.impl

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.FeedMessageResponse
import dev.sotoestevez.allforone.api.schemas.PlainMessage
import dev.sotoestevez.allforone.api.services.FeedService
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import dev.sotoestevez.allforone.vo.feed.TextMessage
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.vo.feed.Message
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
        val msg1 = TextMessage("1","message1", User(id = "id1", displayName = "name1"))
        val msg2 = TextMessage("2","message2", User(id = "id2", displayName = "name2"))

        val feedMessageResponse = FeedMessageResponse(arrayOf(
            PlainMessage(_id = msg1.id, message = msg1.message, submitter = msg1.submitter.id!!, username = msg1.submitter.displayName!!,
                timestamp = msg1.timestamp, type = Message.Type.TEXT, lastUpdate = msg1.lastUpdate),
            PlainMessage(_id = msg2.id, message = msg2.message, submitter = msg2.submitter.id!!, username = msg2.submitter.displayName!!,
                timestamp = msg2.timestamp, type = Message.Type.TEXT, lastUpdate = msg2.lastUpdate)
        ))
        val response: NetworkResponse.Success<FeedMessageResponse> = mockk()
        coEvery { response.code } returns 200
        coEvery { response.body } returns feedMessageResponse
        coEvery { mockFeedService.getMessages(any(), any()) } returns response

        val result = repo.getMessages(1, "token")

        coVerify(exactly = 1) { mockFeedService.getMessages("token", 1) }
        Assert.assertEquals(result.size, 2)
        Assert.assertEquals(result[0], msg1)
        Assert.assertEquals(result[1], msg2)    // Most recent first
    }

}