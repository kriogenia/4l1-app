package dev.sotoestevez.allforone.repositories

/** Repository in charge of the Global Room connections and events */
interface GlobalRoomRepository {

	/** Events managed by the Global Room Repository **/
	enum class Events(internal val path: String) {
		/** Event sent to the socket when it's successfully connected */
		CONNECT("connect"),
		/** Event to subscribe to the global room*/
		SUBSCRIBE("global:subscribe"),
		/** Even sent to the socket when a subscription to the global room is confirmed */
		SUBSCRIPTION("global:subscription"),
		/** Event sent to the socket when a user of the same global room starts sharing its location */
		SHARING_LOCATION("global:sharing_location")
	}

	/**
	 * Connects the socket of the application to the server and launches a subscription to the assigned global room
	 *
	 * @param userId    id of the current User
	 */
	fun connect(userId: String)

	/**
	 * Subscribes the socket to the event thrown when a Bond starts sharing its location
	 *
	 * @param callback  Event listener, receives the name of the subscribed user
	 */
	fun onSharingLocation(callback: (name: String) -> Unit)

}