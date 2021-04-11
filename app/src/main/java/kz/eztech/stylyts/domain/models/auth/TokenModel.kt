package kz.eztech.stylyts.domain.models.auth

data class TokenModel(
    val refresh: String,
    val access: String
)