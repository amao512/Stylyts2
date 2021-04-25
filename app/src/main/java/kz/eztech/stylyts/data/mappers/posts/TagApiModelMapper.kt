package kz.eztech.stylyts.data.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.TagApiModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class TagApiModelMapper @Inject constructor() {

    fun map(data: List<TagApiModel>?): List<TagModel> {
        data ?: return emptyList()

        return data.map {
            TagModel(
                id = it.id ?: 0,
                title = it.title ?: EMPTY_STRING,
                pointY = it.pointY ?: 0.0,
                pointX = it.pointX ?: 0.0
            )
        }
    }
}