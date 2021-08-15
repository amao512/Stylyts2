package kz.eztech.stylyts.utils.mappers.posts

import kz.eztech.stylyts.global.data.models.posts.TagsApiModel
import kz.eztech.stylyts.global.domain.models.posts.TagsModel

fun TagsApiModel?.map(): TagsModel {
    return TagsModel(
        clothesTags = this?.clothesTags.map(),
        usersTags = this?.usersTags.map()
    )
}