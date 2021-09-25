package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.vo.User

/** Repository to manage all the feed and messaging related operations */
interface FeedRepository: SocketRepository {

	/**
	 * Sends a message from the repository
	 *
	 * @param message	Message to send
	 */
	fun send(message: Message)

	/**
	 * Retrieves a new page of messages
	 *
	 * @param page	Page to retrieve
	 * @param token Authentication token
	 */
	suspend fun getMessages(page: Int, token: String): List<Message>

	/**
	 * Subscribes the callback to new messages updates on the feed room
	 *
	 * @param callback  Event listener, receives the message
	 */
	fun onNewMessage(callback: (Message) -> Unit)

	/**
	 * Subscribes the callback to updates of users joining the room
	 *
	 * @param callback  Event listener, receives the message
	 */
	fun onUserJoining(callback: (String) -> Unit)

}