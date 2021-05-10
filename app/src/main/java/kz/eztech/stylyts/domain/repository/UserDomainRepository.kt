package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel

interface UserDomainRepository {

    fun getFollowersById(
        token: String,
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>>

    fun getFollowingsById(
        token: String,
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>>

    fun followUser(
        token: String,
        userId: String
    ): Single<FollowSuccessModel>

    fun unfollowUser(
        token: String,
        userId: String
    ): Single<Any>
}