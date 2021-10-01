package dev.sotoestevez.allforone.api.services

import com.haroldadmin.cnradapter.NetworkResponse
import dev.sotoestevez.allforone.api.schemas.FeedMessageResponse
import dev.sotoestevez.allforone.api.schemas.PlainMessage
import dev.sotoestevez.allforone.util.rules.CoroutineRule
import dev.sotoestevez.allforone.util.rules.WebServerRule
import dev.sotoestevez.allforone.util.webserver.FeedDispatcher
import dev.sotoestevez.allforone.vo.Message
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FeedServiceTest {

    @get:Rule
    var coroutineRule: CoroutineRule = CoroutineRule()

    @get:Rule
    var webServerRule: WebServerRule = WebServerRule()

    // Object to test
    private val api: FeedService = webServerRule.factory.create(FeedService::class.java)

    @Before
    fun beforeEach() {
        // Set /feed endpoint mock dispatcher
        webServerRule.mock.dispatcher = FeedDispatcher
    }

    @Test
    fun `should parse FeedMessageResponse`(): Unit = runBlocking {
        val expected = FeedMessageResponse(arrayOf(
            PlainMessage("id","message", "id", "name", 0, Message.Type.TEXT),
            PlainMessage("id1","message1", "id1", "name1", 1, Message.Type.TASK)
        ))

        val actual = api.messages("valid", 1)

        Assert.assertTrue(actual is NetworkResponse.Success)
        Assert.assertEquals(expected, (actual as NetworkResponse.Success).body)
    }

}