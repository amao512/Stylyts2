package kz.eztech.stylyts.auth.domain.models

data class TokenModel(
    val refresh: String,
    val access: String
)