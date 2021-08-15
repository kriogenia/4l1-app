package dev.sotoestevez.allforone.api

import dev.sotoestevez.allforone.api.data.BaseErrorResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class APIErrorExceptionTest {

    @Test
    fun constructorErrorResponse_withError() {
        val error = APIErrorException(BaseErrorResponse("error"))
        assertEquals("error", error.message)
    }

    @Test
    fun constructorErrorResponse_withoutError() {
        val error = APIErrorException(null)
        assertEquals("An unexpected error has occurred", error.message)
    }

}