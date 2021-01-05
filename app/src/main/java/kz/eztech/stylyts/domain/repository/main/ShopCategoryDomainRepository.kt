package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.domain.models.Style

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface ShopCategoryDomainRepository {
    fun getCategories():Single<ShopCategoryModel>
    fun getCategoryDetail(data:HashMap<String,Any>):Single<CategoryTypeDetailModel>
    fun getStyles(token:String):Single<List<Style>>
    fun saveCollection(token:String,model: CollectionPostCreateModel):Single<Unit>
}