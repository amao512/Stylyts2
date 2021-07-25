package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel

interface UserDomainRepository {

    fun getFollowersById(
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>>

    fun getFollowingsById(
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>>

    fun followUser(userId: String): Single<FollowSuccessModel>

    fun unfollowUser(userId: String): Single<Any>
}