package kz.eztech.stylyts.data.exception

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ApiError {
    val errors: List<ErrorField> = ArrayList()
    val detail:String = ""
}

class ErrorField{
    val field = ""
    val error_code = ""
    val message = ""
}