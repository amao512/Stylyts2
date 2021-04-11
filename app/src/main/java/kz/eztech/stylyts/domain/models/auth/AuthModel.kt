package kz.eztech.stylyts.domain.models.auth

import kz.eztech.stylyts.domain.models.user.UserModel

data class AuthModel(
    val token: TokenModel,
    val user: UserModel
)