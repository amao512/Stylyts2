package kz.eztech.stylyts.data.api

import kz.eztech.stylyts.BuildConfig


/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
object RestConstants {
    const val BASE_URL: String = BuildConfig.BASE_URL
    
    const val REGISTER_USER: String = "auth/"
    const val LOGIN_USER: String = "auth/login/"
    const val GENERATE_FORGOT_PASSWORD: String = "auth/generate_forgot_password/"
    const val SET_NEW_PASSWORD: String = "auth/set_new_password/"
    //User
    const val GET_USER_PROFILE: String = "auth/me/"

    //ShopCategories
    const val GET_CATEGORIES: String = "clothes/category/"
    const val GET_CATEGORIES_DETAIL: String = "clothes/type/{id}/"
    const val GET_STYLES: String = "clothes/styles/"
    const val SAVE_COLLECTION: String = "outfit/"
    const val UPDATE_COLLECTION: String = "outfit/{id}/"
    const val GET_COLLECTIONS: String = "outfit/"
    const val GET_ITEM_DETAIL: String = "clothes/item/{id}/"
    const val GET_FILTERED_ITEMS: String = "clothes/item/"
    const val GET_ITEM_BY_BARCODE: String = "clothes/item/get_by_barcode/"

    const val SAVE_ITEM:String = "clothes/items/"

    const val GET_BRANDS:String = "clothes/brands/"
    
    const val SAVE_COLLECTION_TO_ME = "outfit/{id}/save/"
    
    const val SEARCH_USER_BY_NAME = "auth/search/"

    const val SAVE_ITEM_BY_PHOTO = "clothes/item/"
    
    

}