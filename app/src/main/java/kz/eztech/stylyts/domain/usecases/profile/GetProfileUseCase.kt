package kz.eztech.stylyts.domain.usecases.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.profile.ProfileDomainRepository
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
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var token: String

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.getUserProfile(token)
    }

    fun initParams(token: String) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
    }
}