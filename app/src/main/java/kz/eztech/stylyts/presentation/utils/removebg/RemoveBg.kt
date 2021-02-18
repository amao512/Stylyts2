package kz.eztech.stylyts.presentation.utils.removebg

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.File
import java.io.IOException

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
object RemoveBg {

    private const val API_ENDPOINT = "https://api.remove.bg/v1.0/removebg"

    private var apiKey: String? = null

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private val gson by lazy {
        GsonBuilder().create()
    }

    fun init(apiKey: String) {
        RemoveBg.apiKey = apiKey
    }


    fun from(file: File, callback: RemoveBgCallback) {

        require(apiKey != null) { "You must call RemoveBg.init before calling RemoveBg.from" }

        val filePart = CountingFileRequestBody(
            file,
            "image/png",
            object : CountingFileRequestBody.ProgressListener {
                override fun transferred(percentage: Float) {
                    callback.onUploadProgress(percentage)
                    if (percentage >= 100) {
                        callback.onProcessing()
                    }
                }

            })

        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("size", "auto")
            .addFormDataPart("image_file", "image_file", filePart)
            .build()


        val request = Request.Builder()
            .url(API_ENDPOINT)
            .addHeader("X-Api-Key", apiKey!!)
            .post(body)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {

                if (response.isSuccessful) {

                    // success, converting to bitmap
                    response.body!!.byteStream().let { bytesStream ->
                        val bmp = BitmapFactory.decodeStream(bytesStream)
                        callback.onSuccess(bmp)
                    }

                } else {

                    // error, parsing error.
                    val jsonResp = response.body!!.string()
                    val errorResp = gson.fromJson<ErrorResponse>(jsonResp, ErrorResponse::class.java)
                    callback.onError(errorResp.errors)
                }
            }
        })

    }

    interface RemoveBgCallback {
        fun onUploadProgress(progress: Float)
        fun onProcessing()
        fun onSuccess(bitmap: Bitmap)
        fun onError(errors: List<ErrorResponse.Error>)
    }
}