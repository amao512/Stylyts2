package kz.eztech.stylyts.presentation.utils.extensions.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.TagApiModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<TagApiModel>?.map(): List<TagModel> {
    this ?: return emptyList()

    return this.map {
        TagModel(
            id = it.id ?: 0,
            title = it.title ?: EMPTY_STRING,
            pointY = it.pointY ?: 0.0,
            pointX = it.pointX ?: 0.0
        )
    }
}