package kz.eztech.stylyts.data.api

import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants.GENERATE_FORGOT_PASSWORD
import kz.eztech.stylyts.data.api.RestConstants.GET_CATEGORIES
import kz.eztech.stylyts.data.api.RestConstants.GET_CATEGORIES_DETAIL
import kz.eztech.stylyts.data.api.RestConstants.GET_COLLECTIONS
import kz.eztech.stylyts.data.api.RestConstants.GET_STYLES
import kz.eztech.stylyts.data.api.RestConstants.GET_USER_PROFILE
import kz.eztech.stylyts.data.api.RestConstants.LOGIN_USER
import kz.eztech.stylyts.data.api.RestConstants.REGISTER_USER
import kz.eztech.stylyts.data.api.RestConstants.SAVE_COLLECTION
import kz.eztech.stylyts.data.api.RestConstants.SET_NEW_PASSWORD
import kz.eztech.stylyts.domain.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
interface API {
    
    @FormUrlEncoded
    @POST(REGISTER_USER)
    fun registerUser(
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("first_name") first_name: String,
            @Field("last_name") last_name: String,
            @Field("date_of_birth") date_of_birth: String,
            @Field("should_send_mail") should_send_mail: Boolean,
    ): Single<Response<UserModel>>
    
    @FormUrlEncoded
    @POST(LOGIN_USER)
    fun loginUser( @Field("username") username: String,
                   @Field("password") password: String): Single<Response<UserModel>>
    
    @FormUrlEncoded
    @POST(GENERATE_FORGOT_PASSWORD)
    fun generateForgotPassword(@Field("email") email: String): Single<Response<Unit>>

    @FormUrlEncoded
    @POST(SET_NEW_PASSWORD)
    fun setNewPassword(@Field("token") token: String,
                       @Field("password") password: String): Single<Response<Unit>>

    @GET(GET_CATEGORIES)
    fun getCategories(): Single<Response<ShopCategoryModel>>

    @GET(GET_CATEGORIES_DETAIL)
    fun getCategoriesDetail(@Path("id")id:Int,
                            @Query("gender")gender_type:String): Single<Response<CategoryTypeDetailModel>>
    
    @GET(GET_USER_PROFILE)
    fun getUserProfile(@Header("Authorization")token: String): Single<Response<UserModel>>

    @GET(GET_STYLES)
    fun getStyles(@Header("Authorization")token: String): Single<Response<List<Style>>>
    
    @Multipart
    @POST(SAVE_COLLECTION)
    fun saveCollection(@Header("Authorization") token: String,
                       @Part("text") text: RequestBody,
                       @Part data: MultipartBody.Part): Single<Response<Unit>>
    /*@POST(SAVE_COLLECTION)
    fun saveCollection(@Header("Authorization") token: String,
                       @Body file:MultipartBody): Single<Response<Unit>>*/
    
    @GET(GET_COLLECTIONS)
    fun getCollections(@Header("Authorization") token: String): Single<Response<MainLentaModel>>
}