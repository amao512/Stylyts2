package kz.eztech.stylyts.utils.mappers.user

import kz.eztech.stylyts.global.data.models.user.FollowSuccessApiModel
import kz.eztech.stylyts.global.domain.models.user.FollowSuccessModel

fun FollowSuccessApiModel?.map(): FollowSuccessModel {
    return FollowSuccessModel(
        id = this?.id ?: 0,
        createdAt = this?.createdAt.orEmpty(),
        modifiedAt = this?.modifiedAt.orEmpty(),
        follower = this?.follower ?: 0,
        followee = this?.followee ?: 0
    )
}