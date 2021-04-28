package kz.eztech.stylyts.data.mappers.user

import kz.eztech.stylyts.data.api.models.user.FollowerApiModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class FollowerApiModelMapper @Inject constructor() {

    fun map(data: List<FollowerApiModel>?): List<FollowerModel> {
        data ?: return emptyList()

        return data.map {
            FollowerModel(
                id = it.id ?: 0,
                username = it.username ?: EMPTY_STRING,
                firstName = it.firstName ?: EMPTY_STRING,
                lastName = it.lastName ?: EMPTY_STRING,
                avatar = it.avatar ?: EMPTY_STRING,
                isAlreadyFollow = it.isAlreadyFollow
            )
        }
    }
}