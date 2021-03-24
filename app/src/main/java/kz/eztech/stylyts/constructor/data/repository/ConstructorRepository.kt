package kz.eztech.stylyts.constructor.data.repository

import io.reactivex.Single
import kz.eztech.stylyts.common.data.exception.NetworkException
import kz.eztech.stylyts.constructor.data.api.ConstructorApi
import kz.eztech.stylyts.constructor.domain.models.PostModel
import kz.eztech.stylyts.constructor.domain.repository.ConstructorDomainRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class ConstructorRepository @Inject constructor(
    private val api: ConstructorApi
) : ConstructorDomainRepository {

    override fun createPost(
        token: String,
        description: String,
        hidden: Boolean,
        tags: String,
        imageOne: MultipartBody.Part
    ): Single<PostModel> {
        return api.createPost(
            token = token,
            description = description,
            hidden = hidden,
            tags = tags,
            imageOne = imageOne
        ).map {
            when (it.isSuccessful) {
                true -> it.body()
                else -> throw NetworkException(it)
            }
        }
    }
}