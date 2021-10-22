package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.feed.Message

/** Repository to manage all the feed and messaging related operations */
interface FeedRepository : SocketRoomRepository {

    /**
     * Sends a message from the repository
     *
     * @param message    Message to send
     */
    fun send(message: Message)

    /**
     * Retrieves a new page of messages
     *
     * @param page    Page to retrieve
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
     * Subscribes the callback to deletion of messages on the feed room
     *
     * @param callback  Event listener, receives the message
     */
    fun onMessageDeleted(callback: (Message) -> Unit)

    /**
     * Subscribes the callback to updates of users joining the room
     *
     * @param callback  Event listener, receives the message
     */
    fun onUserJoining(callback: (String) -> Unit)

    /**
     * Subscribes the callback to updates of users leaving the room
     *
     * @param callback  Event listener, receives the message
     */
    fun onUserLeaving(callback: (String) -> Unit)

}