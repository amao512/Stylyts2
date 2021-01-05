package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.repository.main.ProfileDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class ProfileRepository: ProfileDomainRepository {
	private var api: API
	
	@Inject
	constructor(api: API){
		this.api = api
	}
	
	override fun getProfile(token:String): Single<UserModel> {
		return api.getUserProfile(token).map {
			when(it.isSuccessful){
				true -> it.body()
				else ->  throw NetworkException(it)
			}
		}
	}
}