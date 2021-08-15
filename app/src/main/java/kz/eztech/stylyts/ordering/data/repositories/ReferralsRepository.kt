package kz.eztech.stylyts.ordering.data.repositories

import io.reactivex.Single
import kz.eztech.stylyts.ordering.data.api.ReferralApi
import kz.eztech.stylyts.global.data.exception.NetworkException
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.ordering.domain.repositories.ReferralsDomainRepository
import kz.eztech.stylyts.utils.mappers.map
import kz.eztech.stylyts.utils.mappers.referrals.map
import javax.inject.Inject

class ReferralsRepository @Inject constructor(
    private val api: ReferralApi
) : ReferralsDomainRepository {

    override fun getReferrals(map: Map<String, String>): Single<ResultsModel<ReferralModel>> {
        return api.getReferrals(map).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getReferralById(referralId: Int): Single<ReferralModel> {
        return api.getReferralById(referralId).map {
            when (it.isSuccessful) {
                true -> it.body().map()
                false -> throw NetworkException(it)
            }
        }
    }
}