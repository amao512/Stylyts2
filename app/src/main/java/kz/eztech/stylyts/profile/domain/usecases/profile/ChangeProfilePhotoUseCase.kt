package kz.eztech.stylyts.profile.domain.usecases.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.profile.domain.repositories.ProfileDomainRepository
import kz.eztech.stylyts.global.domain.usecases.BaseUseCase
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Named

class ChangeProfilePhotoUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var profileDomainRepository: ProfileDomainRepository
) : BaseUseCase<UserModel>(executorThread, uiThread) {

    private lateinit var avatar: MultipartBody.Part

    override fun createSingleObservable(): Single<UserModel> {
        return profileDomainRepository.setProfilePhoto(avatar)
    }

    fun initParams(avatar: MultipartBody.Part) {
        this.avatar = avatar
    }
}