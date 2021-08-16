package dev.sotoestevez.allforone.util.rules

import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor

/**
 * Test rule for test suites with coroutine functions.
 * It provides a DispatcherProvider to mock all the Dispatchers and to perform a runBlockingTest.
 * It also sets the main dispatcher and also cleans the coroutines
 *
 * @property testDispatcher Dispatcher to use in the tests, by default it is a [TestCoroutineDispatcher]
 */
@ExperimentalCoroutinesApi
class CoroutineRule(val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    /**
     * Mock of DispatcherProvider to focus tests into a given thread
     */
    val testDispatcherProvider: DispatcherProvider = object: DispatcherProvider {
        override fun default(): CoroutineDispatcher = testDispatcher
        override fun io(): CoroutineDispatcher = testDispatcher
        override fun main(): CoroutineDispatcher = testDispatcher
        override fun unconfined(): CoroutineDispatcher = testDispatcher
    }

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

}