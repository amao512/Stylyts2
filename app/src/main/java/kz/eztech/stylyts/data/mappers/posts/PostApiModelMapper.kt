package kz.eztech.stylyts.data.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class PostApiModelMapper @Inject constructor(
    private val tagsApiModelMapper: TagsApiModelMapper
) {

    fun map(data: List<PostApiModel>?): List<PostModel> {
        data ?: return emptyList()

        return data.map {
            PostModel(
                id = it.id ?: 0,
                description = it.description ?: EMPTY_STRING,
                author = it.author ?: 0,
                images = it.images ?: emptyList(),
                tags = tagsApiModelMapper.map(data = it.tags),
                hidden = it.hidden
            )
        }
    }

    fun map(data: PostApiModel?): PostModel {
        return PostModel(
            id = data?.id ?: 0,
            description = data?.description ?: EMPTY_STRING,
            author = data?.author ?: 0,
            images = data?.images ?: emptyList(),
            tags = tagsApiModelMapper.map(data = data?.tags),
            hidden = data?.hidden ?: false
        )
    }
}