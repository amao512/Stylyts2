package kz.eztech.stylyts.profile.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationsModel
import kz.eztech.stylyts.common.domain.models.UserModel
import kz.eztech.stylyts.search.domain.models.SearchModel

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
interface ProfileDomainRepository {

    fun getUserProfile(token: String): Single<UserModel>

    fun editUser(
        token: String,
        data: HashMap<String, Any>
    ): Single<UserModel>

    fun getUserProfileById(
        token: String,
        userId: String
    ): Single<UserModel>

    fun getMyPublications(token: String): Single<SearchModel<PublicationsModel>>
}