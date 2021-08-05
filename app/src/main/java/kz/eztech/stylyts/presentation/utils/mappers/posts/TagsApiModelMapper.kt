package kz.eztech.stylyts.presentation.utils.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.posts.TagsModel

fun TagsApiModel?.map(): TagsModel {
    return TagsModel(
        clothesTags = this?.clothesTags.map(),
        usersTags = this?.usersTags.map()
    )
}