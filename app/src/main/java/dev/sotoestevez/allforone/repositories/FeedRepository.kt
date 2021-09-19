package dev.sotoestevez.allforone.repositories

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.schemas.UserInfoMsg
import dev.sotoestevez.allforone.data.User

/**
 * Repository to manage all the feed and messaging related operations
 *
 * @constructor
 *
 * @param gson   Gson serializer/deserializer
 */
class FeedRepository(gson: Gson = Gson()): BaseSocketRepository(gson) {

	/** Events managed by the Feed Repository **/
	enum class Events(internal val id: String) {
		/** Event to notify that the user joined the feed room */
		JOIN("feed:join"),
		/** Event to send a message through the feed */
		SEND("feed:send")
	}

	// TODO extract join and leave to SocketInterface

	fun join(user: User) {
		// do something else on JOINED notification?
		socket.emit(Events.JOIN.id, toJson(UserInfoMsg(user.id!!, user.displayName!!)))
	}

}