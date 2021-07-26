package kz.eztech.stylyts.domain.usecases.referrals

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.referrals.ReferralModel
import kz.eztech.stylyts.domain.repository.ReferralsDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class GetReferralUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val referralsDomainRepository: ReferralsDomainRepository
) : BaseUseCase<ReferralModel>(executorThread, uiThread) {

    private var referralId: Int = 0

    override fun createSingleObservable(): Single<ReferralModel> {
        return referralsDomainRepository.getReferralById(referralId)
    }

    fun initParams(referralId: Int) {
        this.referralId = referralId
    }
}