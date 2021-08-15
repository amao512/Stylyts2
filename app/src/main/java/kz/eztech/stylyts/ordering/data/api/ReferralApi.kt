package kz.eztech.stylyts.ordering.data.api

import io.reactivex.Single
import kz.eztech.stylyts.global.data.constants.RestConstants
import kz.eztech.stylyts.global.data.models.ResultsApiModel
import kz.eztech.stylyts.ordering.data.models.referrals.ReferralApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ReferralApi {

    @GET(RestConstants.GET_REFERRALS)
    fun getReferrals(
        @QueryMap map: Map<String, String>
    ): Single<Response<ResultsApiModel<ReferralApiModel>>>

    @GET(RestConstants.GET_REFERRALS_BY_ID)
    fun getReferralById(
        @Path("referral_id") referralId: Int
    ): Single<Response<ReferralApiModel>>
}