package kz.eztech.stylyts.data.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.domain.repository.main.ShopCategoryDomainRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    override fun saveCollection(token: String, model: MultipartBody.Part, data: MultipartBody): Single<Unit> {
        return api.saveCollection(token,data).map {
            when(it.isSuccessful){
                true -> Unit
                false -> throw NetworkException(it)
            }
        }
    }
}