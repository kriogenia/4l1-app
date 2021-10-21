package dev.sotoestevez.allforone.repositories

import dev.sotoestevez.allforone.vo.User

/** Common socket repository functionality */
interface SocketRoomRepository {

    /**
     * Connects the user to the repository room
     *
     * @param user  Current user
     */
    fun join(user: User)

    /**
     * Disconnects the user from the repository room
     *
     * @param user  Current user
     */
    fun leave(user: User)

}