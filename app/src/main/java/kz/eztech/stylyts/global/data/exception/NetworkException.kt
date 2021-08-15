package kz.eztech.stylyts.global.data.exception

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NetworkException(response: Response<*>) : HttpException(response) {

    var responseBody: Response<*> = response

    fun getResponse(): Response<*> {
        return responseBody
    }

    fun getErrorBody(): String {
        var errorBody = ""
        val responseBody = getResponse().errorBody()
        try {
            errorBody = responseBody?.string().toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return errorBody
    }
}