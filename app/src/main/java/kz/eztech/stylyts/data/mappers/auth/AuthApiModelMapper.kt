package kz.eztech.stylyts.data.mappers.auth

import kz.eztech.stylyts.data.api.models.auth.AuthApiModel
import kz.eztech.stylyts.data.mappers.user.UserApiModelMapper
import kz.eztech.stylyts.domain.models.auth.AuthModel
import javax.inject.Inject

class AuthApiModelMapper @Inject constructor(
    private val tokenApiModelMapper: TokenApiModelMapper,
    private val userApiModelMapper: UserApiModelMapper
) {

    fun map(data: AuthApiModel?): AuthModel {
        return AuthModel(
            token = tokenApiModelMapper.map(data?.token),
            user = userApiModelMapper.map(data?.user)
        )
    }
}