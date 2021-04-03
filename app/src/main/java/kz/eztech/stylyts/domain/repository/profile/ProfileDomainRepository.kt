package kz.eztech.stylyts.domain.repository.profile

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.domain.models.ResultsModel

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
interface ProfileDomainRepository {

    fun getUserProfile(token: String): Single<UserModel>

    fun editUserProfile(
        token: String,
        data: HashMap<String, Any?>
    ): Single<UserModel>

    fun getUserProfileById(
        token: String,
        userId: String
    ): Single<UserModel>

    fun getMyPublications(token: String): Single<ResultsModel<PublicationModel>>
}