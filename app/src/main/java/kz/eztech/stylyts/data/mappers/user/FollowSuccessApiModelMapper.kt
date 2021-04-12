package kz.eztech.stylyts.data.mappers.user

import kz.eztech.stylyts.data.api.models.user.FollowSuccessApiModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class FollowSuccessApiModelMapper @Inject constructor() {

    fun map(data: FollowSuccessApiModel?): FollowSuccessModel {
        return FollowSuccessModel(
            id = data?.id ?: 0,
            createdAt = data?.createdAt ?: EMPTY_STRING,
            modifiedAt = data?.modifiedAt ?: EMPTY_STRING,
            follower = data?.follower ?: 0,
            followee = data?.followee ?: 0
        )
    }
}