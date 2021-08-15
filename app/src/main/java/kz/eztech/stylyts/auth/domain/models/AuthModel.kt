package kz.eztech.stylyts.auth.domain.models

import kz.eztech.stylyts.global.domain.models.user.UserModel

data class AuthModel(
    val token: TokenModel,
    val user: UserModel
)