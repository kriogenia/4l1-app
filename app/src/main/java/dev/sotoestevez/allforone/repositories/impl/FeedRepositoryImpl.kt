package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.ApiRequest
import dev.sotoestevez.allforone.api.schemas.PlainMessage
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.api.services.FeedService
import dev.sotoestevez.allforone.repositories.FeedRepository
import dev.sotoestevez.allforone.vo.User
import dev.sotoestevez.allforone.vo.feed.Message

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
		/** Event to leave the room and get notifications of users leaving */
		LEAVE("feed:leave"),
		/** Event to notify clients about new messages on the feed */
		NEW("feed:new"),
		/** Event to join the room and get notifications of users joining */
		JOIN("feed:join"),
		/** Event to send a message through the feed */
		SEND("feed:send")
	}

	override fun join(user: User) {
		socket.emit(Events.JOIN.path, toJson(user.minInfo))
	}

	override fun leave(user: User) {
		socket.emit(Events.LEAVE.path, toJson(user.minInfo))
	}

	override fun send(message: Message) {
		socket.emit(Events.SEND.path, toJson(message.toDto()))
	}

	override suspend fun getMessages(page: Int, token: String): List<Message> {
		val messages = ApiRequest(suspend { service.getMessages(token, page) }).performRequest().messages
		return messages.map { Message.Builder().apply { data = it }.build() }
	}

	override fun onNewMessage(callback: (Message) -> Unit) {	// TODO needs change
		socket.on(Events.NEW.path) {
			val message = fromJson(it, PlainMessage::class.java)
			callback(Message.Builder().apply { data = message }.build())
		}
	}

	override fun onUserJoining(callback: (String) -> Unit) {
		socket.on(Events.JOIN.path) { callback(fromJson(it, UserInfoMsg::class.java).displayName)}
	}

	override fun onUserLeaving(callback: (String) -> Unit) {
		socket.on(Events.LEAVE.path) { callback(fromJson(it, UserInfoMsg::class.java).displayName)}
	}

}