package dev.sotoestevez.allforone.util.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides the [Dispatchers]' types of coroutine dispatchers.
 * This way those dispatchers can be injected and tested.
 */
interface DispatcherProvider {

    /**
     * Main thread dispatcher
     *
     * @return [Dispatchers.Main]
     */
    fun main(): CoroutineDispatcher = Dispatchers.Main

    /**
     * Default dispatcher
     *
     * @return [Dispatchers.Default]
     */
    fun default(): CoroutineDispatcher = Dispatchers.Default

    /**
     * Input/output dispatcher
     *
     * @return [Dispatchers.IO]
     */
    fun io(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Dispatcher not confined to any specific thread
     *
     * @return [Dispatchers.Unconfined]
     */
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined

}