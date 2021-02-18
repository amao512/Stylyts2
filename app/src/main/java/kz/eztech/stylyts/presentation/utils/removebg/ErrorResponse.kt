package kz.eztech.stylyts.presentation.utils.removebg

import com.google.gson.annotations.SerializedName

/**
 * Created by Ruslan Erdenoff on 10.02.2021.
 */
class ErrorResponse(
    @SerializedName("errors")
    val errors: List<Error>
) {

    class Error(
        @SerializedName("title")
        val title: String,
        @SerializedName("detail")
        val detail: String,
        @SerializedName("code")
        val code: String
    )
}