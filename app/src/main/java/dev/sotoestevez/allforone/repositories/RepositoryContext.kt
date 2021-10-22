package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.api.ApiFactory
import dev.sotoestevez.allforone.repositories.impl.*

/**
 * Dependency injector for Repositories
 */
object RepositoryContext {

    private val context: MutableMap<String, Any> = mutableMapOf()

    private const val FEED = "feed"
    private const val GLOBAL = "global"
    private const val LOCATION = "location"
    private const val NOTIFICATION = "notification"
    private const val SESSION = "session"
    private const val TASK = "task"
    private const val USER = "user"

    /** Feed repository instance */
    val feedRepository: FeedRepository
        get() = (context[FEED] ?: FeedRepositoryImpl(ApiFactory.getFeedService()).also { context[FEED] = it }) as FeedRepository

    /** Global room repository instance */
    val globalRoomRepository: GlobalRoomRepository
        get() = (context[GLOBAL] ?: GlobalRoomRepositoryImpl().also { context[GLOBAL] = it }) as GlobalRoomRepository

    /** Location repository instance */
    val notificationRepository: NotificationRepository
        get() = (context[NOTIFICATION] ?: NotificationRepositoryImpl(ApiFactory.getFeedService()).also {
            context[NOTIFICATION] = it
        }) as NotificationRepository

    /** Location repository instance */
    val locationRepository: LocationRepository
        get() = (context[LOCATION] ?: LocationRepositoryImpl().also { context[LOCATION] = it }) as LocationRepository

    /** Session repository instance */
    val sessionRepository: SessionRepository
        get() = (context[SESSION] ?: SessionRepositoryImpl(ApiFactory.getAuthService()).also {
            context[SESSION] = it
        }) as SessionRepository

    /** Task repository instance */
    val taskRepository: TaskRepository
        get() = (context[TASK] ?: TaskRepositoryImpl(ApiFactory.getTaskService()).also { context[TASK] = it }) as TaskRepository

    /** User repository instance */
    val userRepository: UserRepository
        get() = (context[USER] ?: UserRepositoryImpl(ApiFactory.getUserService()).also { context[USER] = it }) as UserRepository

}