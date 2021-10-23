package dev.sotoestevez.allforone.repositories.impl

import com.google.gson.Gson
import dev.sotoestevez.allforone.api.SocketManager
import io.socket.client.Socket
import org.json.JSONObject

/**
 * Base implementation of Socket repositories
 *
 * @property gson   Gson serializer/deserializer
 */
abstract class BaseSocketRepository(private val gson: Gson) {

    /** Current socket */
    protected val socket: Socket = SocketManager.socket

    /**
     * Builds a JSONObject of the specified request to send through the socket
     *
     * @param request   object to convert
     * @return          object in JSONObject format
     */
    protected fun toJson(request: Any): JSONObject {
        return JSONObject(Gson().toJson(request))
    }

    /**
     * Builds the specified object from the passed JSON
     *
     * @param T type of the output object
     * @param received array received through the socket
     * @param type destination class of the parsing
     * @return parsed response
     */
    protected fun <T> fromJson(received: Array<out Any>, type: Class<out T>): T {
        return gson.fromJson(received[0].toString(), type)
    }

}