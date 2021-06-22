package kz.eztech.stylyts.presentation.utils.extensions.mappers.user

import kz.eztech.stylyts.data.api.models.user.FollowSuccessApiModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun FollowSuccessApiModel?.map(): FollowSuccessModel {
    return FollowSuccessModel(
        id = this?.id ?: 0,
        createdAt = this?.createdAt ?: EMPTY_STRING,
        modifiedAt = this?.modifiedAt ?: EMPTY_STRING,
        follower = this?.follower ?: 0,
        followee = this?.followee ?: 0
    )
}