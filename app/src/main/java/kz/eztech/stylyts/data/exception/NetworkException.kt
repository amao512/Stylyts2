package kz.eztech.stylyts.data.exception

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class NetworkException : HttpException {

    var responseBody: Response<*>

    constructor(response: Response<*>) : super(response) {
        this.responseBody = response
    }

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