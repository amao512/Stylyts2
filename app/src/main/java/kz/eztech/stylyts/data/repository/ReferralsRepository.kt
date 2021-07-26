package kz.eztech.stylyts.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.data.api.network.ReferralApi
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.domain.repository.ReferralsDomainRepository
import kz.eztech.stylyts.presentation.utils.extensions.mappers.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.referrals.map
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