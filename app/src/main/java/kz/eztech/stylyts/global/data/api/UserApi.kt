package kz.eztech.stylyts.global.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.global.data.models.user.FollowSuccessApiModel
import kz.eztech.stylyts.global.data.models.user.FollowerApiModel
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @GET(RestConstants.GET_FOLLOWERS_BY_ID)
    fun getUserFollowers(
        @Path("user_id") userId: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<FollowerApiModel>>>

    @GET(RestConstants.GET_FOLLOWINGS_BY_ID)
    fun getUserFollowings(
        @Path("user_id") userId: String,
        @QueryMap queryMap: Map<String, String>
    ): Single<Response<ResultsApiModel<FollowerApiModel>>>

    @POST(RestConstants.FOLLOW_USER_BY_ID)
    fun followUser(
        @Path("user_id") userId: String
    ): Single<Response<FollowSuccessApiModel>>

    @POST(RestConstants.UNFOLLOW_USER_BY_ID)
    fun unfollowUser(
        @Path("user_id") userId: String
    ): Single<Any>
}