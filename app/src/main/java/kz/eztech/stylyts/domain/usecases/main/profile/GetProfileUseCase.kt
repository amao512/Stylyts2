package kz.eztech.stylyts.domain.usecases.main.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ProfileModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.AuthorizationDomainRepository
import kz.eztech.stylyts.domain.repository.main.ProfileDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class GetProfileUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<ProfileModel>(executorThread, uiThread) {
    private lateinit var token: String

    fun initParams(token: String) {
        this.token = "JWT $token"
    }

    override fun createSingleObservable(): Single<ProfileModel> {
        return profileDomainRepository.getProfile(token)
    }
}