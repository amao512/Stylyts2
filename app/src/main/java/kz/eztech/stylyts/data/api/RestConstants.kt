package kz.eztech.stylyts.data.api

import kz.eztech.stylyts.BuildConfig


/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
object RestConstants {
    const val BASE_URL: String = BuildConfig.BASE_URL
    const val HEADERS_AUTH_FORMAT = "JWT %s"

    // Authentication
    const val REGISTER_USER: String = "auth/signup"
    const val LOGIN_USER: String = "auth/token"
    const val IS_USERNAME_EXISTS = "auth/username-exists"
    const val GENERATE_FORGOT_PASSWORD: String = "auth/generate_forgot_password/"
    const val SET_NEW_PASSWORD: String = "auth/set_new_password/"

    // Token
    const val VERIFY_TOKEN: String = "auth/token/verify"
    const val REFRESH_TOKEN: String = "auth/token/refresh"

    // User
    const val GET_MY_PROFILE: String = "profiles/me"
    const val EDIT_PROFILE: String = "profiles/me"
    const val GET_USER_BY_ID: String = "profiles/{user_id}"
    const val GET_FOLLOWERS_BY_ID: String = "profiles/{user_id}/followers"
    const val GET_FOLLOWINGS_BY_ID: String = "profiles/{user_id}/followings"
    const val FOLLOW_USER_BY_ID: String = "profiles/{user_id}/follow"
    const val UNFOLLOW_USER_BY_ID: String = "profiles/{user_id}/unfollow"

    // Address
    const val POST_ADDRESS: String = "addresses"
    const val GET_ALL_ADDRESS: String = "addresses"
    const val DELETE_ADDRESS: String = "addresses/{address_id}"

    // Search
    const val SEARCH_USER_BY_USERNAME: String = "profiles"
    const val SEARCH_CLOTHES_BY_TITLE: String = "clothes"

    // Clothes
    const val GET_CLOTHES: String = "clothes"
    const val GET_CLOTHES_BY_ID = "clothes/{id}"
    const val GET_CLOTHES_CATEGORIES: String = "clothes/categories"
    const val GET_CLOTHES_TYPES: String = "clothes/types"
    const val GET_CLOTHES_STYLES: String = "clothes/styles"
    const val GET_CLOTHES_BRANDS: String = "clothes/brands"
    const val GET_CLOTHES_BRAND_BY_ID: String = "clothes/brands/{brand_id}"
    const val SAVE_CLOTHES_TO_WARDROBE: String = "clothes/{clothes_id}/save"

    // Outfits
    const val GET_OUTFITS = "outfits"
    const val CREATE_OUTFIT: String = "outfits"

    // Post
    const val CREATE_POST: String = "post/"
    const val GET_MY_POSTS: String = "post/"

    //ShopCategories
    const val GET_CATEGORIES: String = "clothes/category/"
    const val GET_CATEGORIES_DETAIL: String = "clothes/type/{id}/"
    const val GET_STYLES: String = "clothes/styles/"
    const val UPDATE_COLLECTION: String = "outfit/{id}/"
    const val GET_COLLECTIONS: String = "outfit/"
    const val GET_ITEM_DETAIL: String = "clothes/item/{id}/"
    const val GET_FILTERED_ITEMS: String = "clothes/item/"
    const val GET_ITEM_BY_BARCODE: String = "clothes/item/get_by_barcode/"

    const val SAVE_ITEM: String = "clothes/items/"

    const val GET_BRANDS: String = "clothes/brands/"
    
    const val SAVE_COLLECTION_TO_ME = "outfit/{id}/save/"
    
    const val SEARCH_USER_BY_NAME = "auth/search/"

    const val SAVE_ITEM_BY_PHOTO = "clothes/item/"
}