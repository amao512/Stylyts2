package kz.eztech.stylyts.presentation.utils.mappers.user

import kz.eztech.stylyts.data.api.models.user.FollowerApiModel
import kz.eztech.stylyts.domain.models.user.FollowerModel

fun List<FollowerApiModel>?.map(): List<FollowerModel> {
    this ?: return emptyList()

    return this.map {
        FollowerModel(
            id = it.id ?: 0,
            username = it.username.orEmpty(),
            firstName = it.firstName.orEmpty(),
            lastName = it.lastName.orEmpty(),
            avatar = it.avatar.orEmpty(),
            isAlreadyFollow = it.isAlreadyFollow
        )
    }
}