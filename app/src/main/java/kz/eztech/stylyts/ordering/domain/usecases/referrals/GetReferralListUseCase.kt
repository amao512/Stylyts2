package kz.eztech.stylyts.ordering.domain.usecases.referrals

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.common.ResultsModel
import kz.eztech.stylyts.ordering.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.ordering.domain.repositories.ReferralsDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetReferralListUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val referralsDomainRepository: ReferralsDomainRepository
) : BaseUseCase<ResultsModel<ReferralModel>>(executorThread, uiThread) {

    private lateinit var map: Map<String, String>

    override fun createSingleObservable(): Single<ResultsModel<ReferralModel>> {
        return referralsDomainRepository.getReferrals(map)
    }

    fun initParams(page: Int = 1) {
        val map = HashMap<String, String>()
        map["page"] = page.toString()

        this.map = map
    }
}