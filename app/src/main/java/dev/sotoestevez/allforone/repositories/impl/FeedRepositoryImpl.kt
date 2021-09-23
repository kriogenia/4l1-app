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

	override fun join(user: User) {
		// do something else on JOINED notification?
		socket.emit(FeedRepository.Events.JOIN.path, toJson(user.minInfo))
	}

	override fun send(msg: Message) {
		socket.emit(FeedRepository.Events.SEND.path, toJson(FeedMsg(msg.message, msg.user.minInfo)))
	}

	override suspend fun getMessages(page: Int, token: String): List<Message> {
		val messages = ApiRequest(suspend { service.messages(token, page) }).performRequest().messages
		return messages.map { Message(it._id, it.message, User(id = it.user, displayName = it.username), it.type, it.timestamp) }
	}

	override fun onNewMessage(callback: (Message) -> Unit) {
		socket.on(FeedRepository.Events.NEW.path) { callback(fromJson(it, Message::class.java)) }
	}

}