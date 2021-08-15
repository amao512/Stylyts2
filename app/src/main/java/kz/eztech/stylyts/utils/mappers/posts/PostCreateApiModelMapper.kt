package kz.eztech.stylyts.utils.mappers.posts

import kz.eztech.stylyts.global.data.models.posts.PostCreateApiModel
import kz.eztech.stylyts.global.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.utils.EMPTY_STRING
import java.io.File

fun PostCreateApiModel?.map(): PostCreateModel {
    return PostCreateModel(
        id = this?.id ?: 0,
        description = this?.description.orEmpty(),
        clothesList = emptyList(),
        userList = emptyList(),
        images = emptyList(),
        imageFile = File(EMPTY_STRING)
    )
}