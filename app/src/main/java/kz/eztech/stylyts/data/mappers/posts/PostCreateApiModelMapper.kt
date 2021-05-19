package kz.eztech.stylyts.data.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.PostCreateApiModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import java.io.File
import javax.inject.Inject

class PostCreateApiModelMapper @Inject constructor() {

    fun map(data: PostCreateApiModel?): PostCreateModel {
        return PostCreateModel(
            id = data?.id ?: 0,
            description = data?.description ?: EMPTY_STRING,
            clothesList = emptyList(),
            userList = emptyList(),
            images = emptyList(),
            imageFile = File(EMPTY_STRING)
        )
    }
}