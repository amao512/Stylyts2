package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.ClothesMainModel

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
interface ItemDetailDomainRepository {
    fun getItemDetail(token:String,id:Int):Single<ClothesMainModel>
    fun getItemByBarcode(token:String,value:String):Single<Unit>
}