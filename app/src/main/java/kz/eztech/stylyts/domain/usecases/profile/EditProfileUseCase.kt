package kz.eztech.stylyts.domain.usecases.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.profile.ProfileDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

class EditProfileUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var data: HashMap<String, Any?>

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.editUserProfile(token, data)
    }

    fun initParams(
        token: String,
        data: HashMap<String, Any?>
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.data = data
    }
}