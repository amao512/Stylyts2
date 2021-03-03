package kz.eztech.stylyts.domain.repository.main

import io.reactivex.Single
import kz.eztech.stylyts.domain.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
interface ShopCategoryDomainRepository {
    fun getCategories():Single<ShopCategoryModel>
    fun getCategoryDetail(data:HashMap<String,Any>):Single<CategoryTypeDetailModel>
    fun getStyles(token:String):Single<List<Style>>
    fun saveCollection(token:String, data: ArrayList<MultipartBody.Part>):Single<MainResult>
    fun updateCollection(token:String, id:Int,data: ArrayList<MultipartBody.Part>):Single<Unit>
    fun getBrands(token:String):Single<BrandsModel>
    fun saveCollectionToMe(token:String,id:Int):Single<Unit>
   // fun saveCollection(token:String,data: MultipartBody):Single<Unit>
}