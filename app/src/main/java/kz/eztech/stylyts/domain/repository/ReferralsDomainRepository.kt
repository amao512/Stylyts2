package kz.eztech.stylyts.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.referrals.ReferralModel

interface ReferralsDomainRepository {

    fun getReferrals(map: Map<String, String>): Single<ResultsModel<ReferralModel>>

    fun getReferralById(referralId: Int): Single<ReferralModel>
}