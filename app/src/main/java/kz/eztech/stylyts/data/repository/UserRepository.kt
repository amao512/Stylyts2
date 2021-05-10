package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.UserApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.data.mappers.ResultsApiModelMapper
import kz.eztech.stylyts.data.mappers.user.FollowSuccessApiModelMapper
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.user.FollowSuccessModel
import kz.eztech.stylyts.domain.models.user.FollowerModel
import kz.eztech.stylyts.domain.repository.UserDomainRepository
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApi,
    private val resultsApiModelMapper: ResultsApiModelMapper,
    private val followSuccessApiModelMapper: FollowSuccessApiModelMapper
) : UserDomainRepository {

    override fun getFollowersById(
        token: String,
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>> {
        return api.getUserFollowers(
            token = token,
            userId = userId,
            queryMap = queryMap
        ).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getFollowingsById(
        token: String,
        userId: String,
        queryMap: Map<String, String>
    ): Single<ResultsModel<FollowerModel>> {
        return api.getUserFollowings(
            token = token,
            userId = userId,
            queryMap = queryMap
        ).map {
            when (it.isSuccessful) {
                true -> resultsApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun followUser(
        token: String,
        userId: String
    ): Single<FollowSuccessModel> {
        return api.followUser(token, userId).map {
            when (it.isSuccessful) {
                true -> followSuccessApiModelMapper.map(data = it.body())
                false -> throw NetworkException(it)
            }
        }
    }

    override fun unfollowUser(
        token: String,
        userId: String
    ): Single<Any> {
        return api.unfollowUser(token, userId)
    }
}