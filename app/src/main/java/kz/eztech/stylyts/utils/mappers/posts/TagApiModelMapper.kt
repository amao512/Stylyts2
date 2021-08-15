package kz.eztech.stylyts.utils.mappers.posts

import kz.eztech.stylyts.global.data.models.posts.TagApiModel
import kz.eztech.stylyts.global.domain.models.posts.TagModel

fun List<TagApiModel>?.map(): List<TagModel> {
    this ?: return emptyList()

    return this.map {
        TagModel(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            pointY = it.pointY ?: 0.0,
            pointX = it.pointX ?: 0.0
        )
    }
}