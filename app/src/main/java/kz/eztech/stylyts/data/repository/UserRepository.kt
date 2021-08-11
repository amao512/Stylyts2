package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.UserApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.utils.mappers.map
import kz.eztech.stylyts.utils.mappers.user.map
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.repository.UserDomainRepository
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