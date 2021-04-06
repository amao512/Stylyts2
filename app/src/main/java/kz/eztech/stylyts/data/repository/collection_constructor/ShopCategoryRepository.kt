package kz.eztech.stylyts.data.repository.collection_constructor

import io.reactivex.Single
import kz.eztech.stylyts.data.api.API
import kz.eztech.stylyts.data.exception.NetworkException
import kz.eztech.stylyts.domain.models.*
import kz.eztech.stylyts.domain.models.shop.ShopCategoryModel
import kz.eztech.stylyts.domain.repository.collection_constructor.ShopCategoryDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class ShopCategoryRepository @Inject constructor(
    internal var api: API
) : ShopCategoryDomainRepository {

    override fun getCategories(): Single<ShopCategoryModel> {
        return api.getCategories().map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getCategoryDetail(data: HashMap<String, Any>): Single<CategoryTypeDetailModel> {
        return api.getCategoriesDetail(
            data["id"] as Int,
            data["gender_type"] as String
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getStyles(token: String): Single<List<Style>> {
        return api.getStyles(token).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun updateCollection(
        token: String,
        id: Int,
        data: ArrayList<MultipartBody.Part>
    ): Single<Unit> {
        return api.updateCollection(token, id, data).map {
            when (it.isSuccessful) {
                true -> Unit
                false -> throw NetworkException(it)
            }
        }
    }

    override fun saveCollection(
        token: String,
        data: ArrayList<MultipartBody.Part>
    ): Single<MainResult> {
        return api.saveCollection(token, data).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun getBrands(token: String): Single<BrandsModel> {
        return api.getBrands(token).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }

    override fun saveCollectionToMe(token: String, id: Int): Single<Unit> {
        return api.saveCollectionToMe(token, id).map {
            when (it.isSuccessful) {
                true -> it.body()
                false -> throw NetworkException(it)
            }
        }
    }
}