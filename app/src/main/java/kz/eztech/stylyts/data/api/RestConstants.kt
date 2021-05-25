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
    const val SEARCH_USER_BY_USERNAME: String = "profiles/search"
    const val SEARCH_CLOTHES_BY_TITLE: String = "clothes"

    // Clothes
    const val GET_CLOTHES: String = "clothes"
    const val GET_CLOTHES_BY_ID = "clothes/{id}"
    const val GET_CLOTHES_BY_BARCODE = "clothes/by-barcode/{barcode}"
    const val GET_CLOTHES_CATEGORIES: String = "clothes/categories"
    const val GET_CLOTHES_TYPES: String = "clothes/types"
    const val GET_CLOTHES_STYLES: String = "clothes/styles"
    const val GET_CLOTHES_BRANDS: String = "clothes/brands"
    const val GET_CLOTHES_BRAND_BY_ID: String = "clothes/brands/{brand_id}"
    const val GET_CLOTHES_COLORS: String = "clothes/colors"
    const val SAVE_CLOTHES_TO_WARDROBE: String = "clothes/{clothes_id}/save"

    // Wardrobe
    const val CREATE_CLOTHES_BY_PHOTO: String = "wardrobes/add-using-image"

    // Outfits
    const val GET_OUTFITS = "outfits"
    const val GET_OUTFIT_BY_ID = "outfits/{outfit_id}"
    const val CREATE_OUTFIT: String = "outfits"
    const val DELETE_OUTFIT_BY_ID: String = "outfits/{outfit_id}"
    const val UPDATE_OUTFIT: String = "outfits/{outfit_id}"

    // Post
    const val GET_HOMEPAGE_POSTS: String = "homepage"
    const val CREATE_POST: String = "posts"
    const val GET_POSTS: String = "posts"
    const val GET_POST_BY_ID: String = "posts/{post_id}"
    const val DELETE_POST_BY_ID: String = "posts/{post_id}"
    const val UPDATE_POST: String = "posts/{post_id}"
    const val LIKE_POST: String = "posts/{post_id}/like"

    // Comments
    const val GET_COMMENTS: String = "post-comments"
    const val CREATE_COMMENT: String = "post-comments"
}