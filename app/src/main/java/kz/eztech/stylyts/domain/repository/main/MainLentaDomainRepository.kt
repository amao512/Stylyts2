package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.MainLentaModel

/**
 * Created by Ruslan Erdenoff on 14.01.2021.
 */
interface MainLentaDomainRepository {
	fun getCollections(token:String,queries:Map<String,Any>? = null):Single<MainLentaModel>
}