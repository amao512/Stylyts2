package kz.eztech.stylyts.data.exception

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHelper {

    var errorString: String = ""
	@Inject
	constructor()

    fun getApiError(errorObject: JSONObject): ApiError {

        val parser = JsonParser()
        val mJson = parser.parse(errorObject.toString())
        val gson = Gson()

        return gson.fromJson(mJson, ApiError::class.java)
    }

    fun processError(e: Throwable): String {
        try {
	        e as NetworkException
	        val error = JSONObject(e.getErrorBody())
	        val apiError: ApiError = getApiError(error)
			if(apiError.errors.isNotEmpty()){
				errorString = apiError.errors.joinToString { it.message }
			}else if(apiError.detail.isNotEmpty()){
				errorString = apiError.detail
			}else{
				errorString = "Неизвестная ошибка"
			}

        } catch (error: Exception) {
	        errorString = "Неизвестная ошибка"
	        try {
		        when (e) {
			        is NetworkException -> {
				        when (e.code()) {
					        500 -> errorString = ("Ошибка сервера")
					        502 -> errorString = ("Ошибка сервера")
					        404 -> errorString = ("Страницы не существуте")
				        }
			        }
			        is SocketTimeoutException -> errorString = "Нет интернет соединения"

			        is UnknownHostException -> errorString = "Нет интернет соединения"

			        is ConnectException -> errorString = "Нет интернет соединения"
		        }
	        }catch (e:Exception){ }
        }
        return errorString
    }
}