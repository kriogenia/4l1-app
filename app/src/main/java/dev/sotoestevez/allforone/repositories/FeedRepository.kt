package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.User

/** Repository to manage all the feed and messaging related operations */
interface FeedRepository {

	/** Events managed by the Feed Repository **/
	enum class Events(internal val id: String) {
		/** Event to notify clients about new messages on the feed */
		NEW("feed:new"),
		/** Event to notify that the user joined the feed room */
		JOIN("feed:join"),
		/** Event to send a message through the feed */
		SEND("feed:send")
	}

	// TODO extract join and leave to SocketInterface

	/**
	 * Connects the user to the Feed Room
	 *
	 * @param user  current user
	 */
	fun join(user: User)

	/**
	 * Sends a new message through the Feed
	 *
	 * @param msg content of the message
	 */
	fun send(msg: Message)

	/**
	 * Subscribes the callback to new messages updates on the feed room
	 *
	 * @param callback  Event listener, receives the message
	 */
	fun onNewMessage(callback: (Message) -> Unit)

}