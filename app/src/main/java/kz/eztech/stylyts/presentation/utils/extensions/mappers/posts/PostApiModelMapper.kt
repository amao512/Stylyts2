package kz.eztech.stylyts.presentation.utils.extensions.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.comments.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<PostApiModel>?.map(): List<PostModel> {
    this ?: return emptyList()

    return this.map {
        PostModel(
            id = it.id ?: 0,
            description = it.description ?: EMPTY_STRING,
            author = it.author.map(),
            images = it.images ?: emptyList(),
            tags = it.tags.map(),
            hidden = it.hidden,
            clothes = it.clothes.map(),
            commentsCount = it.commentsCount ?: 0,
            likesCount = it.likesCount ?: 0,
            alreadyLiked = it.alreadyLiked,
            firstComment = it.firstComment.map()
        )
    }
}

fun PostApiModel?.map(): PostModel {
    return PostModel(
        id = this?.id ?: 0,
        description = this?.description ?: EMPTY_STRING,
        author = this?.author.map(),
        images = this?.images ?: emptyList(),
        tags = this?.tags.map(),
        hidden = this?.hidden ?: false,
        clothes = this?.clothes.map(),
        commentsCount = this?.commentsCount ?: 0,
        likesCount = this?.likesCount ?: 0,
        alreadyLiked = this?.alreadyLiked ?: false,
        firstComment = this?.firstComment.map()
    )
}