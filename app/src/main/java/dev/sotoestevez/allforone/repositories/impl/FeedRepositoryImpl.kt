package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.FeedMsg
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.api.services.FeedService
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.User

/**
 * Implementation of [FeedRepository]
 *
 * @constructor
 *
 * @param gson   Gson serializer/deserializer
 */
class FeedRepositoryImpl(
	private val service: FeedService,
	gson: Gson = Gson()
): BaseSocketRepository(gson), FeedRepository {

	/** Events managed by the Feed Repository **/
	private enum class Events(val path: String) {
		/** Event to notify clients about new messages on the feed */
		NEW("feed:new"),
		/** Event to notify that the user joined the feed room */
		JOIN("feed:join"),
		/** Event to send a message through the feed */
		SEND("feed:send")
	}

	override fun join(user: User) {
		// do something else on JOINED notification?
		socket.emit(Events.JOIN.path, toJson(user.minInfo))
	}

	override fun leave(user: User) {
		throw NotImplementedError()	// TODO
	}

	override fun send(message: Message) {
		socket.emit(Events.SEND.path, toJson(FeedMsg(message.message, message.user.minInfo)))
	}

	override suspend fun getMessages(page: Int, token: String): List<Message> {
		val messages = ApiRequest(suspend { service.messages(token, page) }).performRequest().messages
		return messages.map { Message(it._id, it.message, User(id = it.user, displayName = it.username), it.type, it.timestamp) }
	}

	override fun onNewMessage(callback: (Message) -> Unit) {
		socket.on(Events.NEW.path) { callback(fromJson(it, Message::class.java)) }
	}

}