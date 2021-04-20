package kz.eztech.stylyts.data.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.TagsApiModel
import kz.eztech.stylyts.domain.models.posts.TagsModel
import javax.inject.Inject

class TagsApiModelMapper @Inject constructor(
    private val tagApiModelMapper: TagApiModelMapper
) {

    fun map(data: TagsApiModel?): TagsModel {
        return TagsModel(
            clothesTags = tagApiModelMapper.map(data = data?.clothesTags),
            usersTags = tagApiModelMapper.map(data = data?.usersTags)
        )
    }
}