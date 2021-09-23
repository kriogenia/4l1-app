package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.FeedMsg
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
class FeedRepositoryImpl(gson: Gson = Gson()): BaseSocketRepository(gson), FeedRepository {

	override fun join(user: User) {
		// do something else on JOINED notification?
		socket.emit(FeedRepository.Events.JOIN.path, toJson(user.minInfo))
	}

	override fun send(msg: Message) {
		socket.emit(FeedRepository.Events.SEND.path, toJson(FeedMsg(msg.message, msg.user.minInfo)))
	}

	override fun onNewMessage(callback: (Message) -> Unit) {
		socket.on(FeedRepository.Events.NEW.path) { callback(fromJson(it, Message::class.java)) }
	}

}