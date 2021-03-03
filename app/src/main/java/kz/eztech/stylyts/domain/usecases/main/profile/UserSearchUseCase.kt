package kz.eztech.stylyts.domain.usecases.main.profile

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.models.UserSearchModel
import kz.eztech.stylyts.domain.repository.main.ProfileDomainRepository
import kz.eztech.stylyts.domain.repository.main.UserSearchDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 22.02.2021.
 */
class UserSearchUseCase : BaseUseCase<List<UserSearchModel>> {
	private var userSearchDomainRepository: UserSearchDomainRepository
	private lateinit var token:String
	private lateinit var username:String
	
	@Inject
	constructor(@Named("executor_thread") executorThread: Scheduler,
	            @Named("ui_thread") uiThread: Scheduler,
	            userSearchDomainRepository: UserSearchDomainRepository
	) :
	super(executorThread, uiThread) {
		this.userSearchDomainRepository = userSearchDomainRepository
	}
	
	fun initParams(token:String,username:String) {
		this.token = "Bearer $token"
		this.username = username
	}
	
	override fun createSingleObservable(): Single<List<UserSearchModel>> {
		return userSearchDomainRepository.getUserByUsername(token,username)
	}
}