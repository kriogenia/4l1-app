package dev.sotoestevez.allforone.model.interfaces

import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONObject

interface WithSocket {

	val socket: Socket

	fun toJson(request: Any): JSONObject {
		return JSONObject(Gson().toJson(request))
	}

}