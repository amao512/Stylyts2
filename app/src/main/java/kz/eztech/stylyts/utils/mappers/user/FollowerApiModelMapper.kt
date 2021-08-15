package kz.eztech.stylyts.utils.mappers.user

import kz.eztech.stylyts.global.data.models.user.FollowerApiModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel

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