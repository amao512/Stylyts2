package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ProfileModel

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
interface ProfileDomainRepository {

    fun getProfile(token: String): Single<ProfileModel>

    fun editProfile(
        token: String,
        data: HashMap<String, Any>
    ): Single<ProfileModel>
}