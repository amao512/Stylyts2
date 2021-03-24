package kz.eztech.stylyts.constructor.domain.repository

import io.reactivex.Single
import kz.eztech.stylyts.constructor.domain.models.PostModel
import okhttp3.MultipartBody

interface ConstructorDomainRepository {

    fun createPost(
        token: String,
        description: String,
        hidden: Boolean,
        tags: String,
        imageOne: MultipartBody.Part
    ): Single<PostModel>
}