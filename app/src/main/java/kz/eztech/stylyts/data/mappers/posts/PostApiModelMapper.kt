package kz.eztech.stylyts.data.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.data.mappers.clothes.ClothesApiModelMapper
import kz.eztech.stylyts.data.mappers.comments.CommentApiModelMapper
import kz.eztech.stylyts.data.mappers.user.UserShortApiModelMapper
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class PostApiModelMapper @Inject constructor(
    private val tagsApiModelMapper: TagsApiModelMapper,
    private val clothesApiModelMapper: ClothesApiModelMapper,
    private val userShortApiModelMapper: UserShortApiModelMapper,
    private val commentApiModelMapper: CommentApiModelMapper
) {

    fun map(data: List<PostApiModel>?): List<PostModel> {
        data ?: return emptyList()

        return data.map {
            PostModel(
                id = it.id ?: 0,
                description = it.description ?: EMPTY_STRING,
                author = userShortApiModelMapper.map(it.author),
                images = it.images ?: emptyList(),
                tags = tagsApiModelMapper.map(data = it.tags),
                hidden = it.hidden,
                clothes = clothesApiModelMapper.map(data = it.clothes),
                commentsCount = it.commentsCount ?: 0,
                likesCount = it.likesCount ?: 0,
                alreadyLiked = it.alreadyLiked,
                firstComment = commentApiModelMapper.map(it.firstComment)
            )
        }
    }

    fun map(data: PostApiModel?): PostModel {
        return PostModel(
            id = data?.id ?: 0,
            description = data?.description ?: EMPTY_STRING,
            author = userShortApiModelMapper.map(data?.author),
            images = data?.images ?: emptyList(),
            tags = tagsApiModelMapper.map(data = data?.tags),
            hidden = data?.hidden ?: false,
            clothes = clothesApiModelMapper.map(data = data?.clothes),
            commentsCount = data?.commentsCount ?: 0,
            likesCount = data?.likesCount ?: 0,
            alreadyLiked = data?.alreadyLiked ?: false,
            firstComment = commentApiModelMapper.map(data?.firstComment)
        )
    }
}