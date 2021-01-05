package kz.eztech.stylyts.domain.usecases.main.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.AuthorizationDomainRepository
import kz.eztech.stylyts.domain.repository.main.ProfileDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class GetProfileUseCase:BaseUseCase<UserModel> {
	private var profileDomainRepository: ProfileDomainRepository
	private lateinit var token:String
	
	@Inject
	constructor(@Named("executor_thread") executorThread: Scheduler,
	            @Named("ui_thread") uiThread: Scheduler,
	            profileDomainRepository: ProfileDomainRepository
	) :
			super(executorThread, uiThread) {
		this.profileDomainRepository = profileDomainRepository
	}
	
	fun initParams(token:String) {
		this.token = "Bearer $token"
	}
	
	override fun createSingleObservable(): Single<UserModel> {
		return profileDomainRepository.getProfile(token)
	}
}