package kz.eztech.stylyts.domain.usecases.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.repository.profile.ProfileDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

class ChangeProfilePhotoUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var avatar: MultipartBody.Part

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.setProfilePhoto(token, avatar)
    }

    fun initParams(
        token: String,
        avatar: MultipartBody.Part
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.avatar = avatar
    }
}