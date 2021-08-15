package kz.eztech.stylyts.global.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.data.api.UserApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.map
import kz.eztech.stylyts.utils.mappers.user.map
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.global.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.global.domain.models.user.FollowerModel
import kz.eztech.stylyts.global.domain.repositories.UserDomainRepository
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi
) : UserDomainRepository {

    override fun getFollowersById(
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>> {
        return api.getUserFollowers(
            userId = userId,
            queryMap = queryMap
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getFollowingsById(
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>> {
        return api.getUserFollowings(
            userId = userId,
            queryMap = queryMap
        ).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun followUser(userId: String): Single<FollowSuccessModel> {
        return api.followUser(userId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun unfollowUser(userId: String): Single<Any> = api.unfollowUser(userId)
}