package dev.sotoestevez.allforone.api

import dev.sotoestevez.allforone.api.responses.BaseErrorResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class APIErrorExceptionTest {

    @Test
    fun `should build the object correctly when given an error`() {
        val error = APIErrorException(BaseErrorResponse("error"))
        assertEquals("error", error.message)
    }

    @Test
    fun `should build the object with the default message when no error or message is provided`() {
        val error = APIErrorException(null)
        assertEquals("An unexpected error has occurred", error.message)
    }

}