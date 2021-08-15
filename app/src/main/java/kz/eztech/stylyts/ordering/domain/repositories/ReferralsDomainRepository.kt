package kz.eztech.stylyts.ordering.domain.repositories

import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralModel

interface ReferralsDomainRepository {

    fun getReferrals(map: Map<String, String>): Single<ResultsModel<ReferralModel>>

    fun getReferralById(referralId: Int): Single<ReferralModel>
}