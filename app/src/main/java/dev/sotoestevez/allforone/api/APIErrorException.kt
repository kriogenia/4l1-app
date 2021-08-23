package dev.sotoestevez.allforone.api

import dev.sotoestevez.allforone.api.responses.ErrorResponse
import java.io.IOException

/** Custom exception to generate from API errors */
class APIErrorException: IOException {

    constructor(message: String): super(message)

    constructor(response: ErrorResponse?): this(
            if (response?.message != null) response.message else "An unexpected error has occurred")

}