package kz.eztech.stylyts.data.api.network

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.api.models.ResultsApiModel
import kz.eztech.stylyts.data.api.models.user.FollowSuccessApiModel
import kz.eztech.stylyts.data.api.models.user.FollowerApiModel
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