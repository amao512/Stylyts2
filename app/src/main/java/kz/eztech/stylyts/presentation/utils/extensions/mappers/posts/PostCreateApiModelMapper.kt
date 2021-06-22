package kz.eztech.stylyts.presentation.utils.extensions.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.PostCreateApiModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import java.io.File

fun PostCreateApiModel?.map(): PostCreateModel {
    return PostCreateModel(
        id = this?.id ?: 0,
        description = this?.description ?: EMPTY_STRING,
        clothesList = emptyList(),
        userList = emptyList(),
        images = emptyList(),
        imageFile = File(EMPTY_STRING)
    )
}