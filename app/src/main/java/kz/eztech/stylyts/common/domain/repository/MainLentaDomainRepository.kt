package kz.eztech.stylyts.common.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.common.domain.models.MainLentaModel

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
interface MainLentaDomainRepository {

    fun getCollections(
        token: String,
        queries: Map<String, Any>? = null
    ): Single<MainLentaModel>
}