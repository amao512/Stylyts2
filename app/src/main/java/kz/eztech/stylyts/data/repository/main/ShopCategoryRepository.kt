package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.CategoryTypeDetailModel
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.ShopCategoryModel
import kz.eztech.stylyts.domain.models.Style
import kz.eztech.stylyts.domain.repository.main.ShopCategoryDomainRepository
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopCategoryRepository:ShopCategoryDomainRepository {
    internal var api: API
    
    @Inject
    constructor(api: API){
        this.api = api
    }
    
    override fun getCategories(): Single<ShopCategoryModel> {
        return api.getCategories().map {
            when(it.isSuccessful){
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getCategoryDetail(data: HashMap<String, Any>): Single<CategoryTypeDetailModel> {
        return api.getCategoriesDetail(
            data["id"] as Int,
            data["gender_type"] as String).map {
            when(it.isSuccessful){
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getStyles(token: String): Single<List<Style>> {
        return api.getStyles(token).map {
            when(it.isSuccessful){
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun saveCollection(token: String, model: CollectionPostCreateModel): Single<Unit> {
        return api.saveCollection(token,model).map {
            when(it.isSuccessful){
                true -> Unit
                false -> throw NetworkException(it)
            }
        }
    }
}