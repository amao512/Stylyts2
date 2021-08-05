package kz.eztech.stylyts.presentation.utils.mappers.posts

import kz.eztech.stylyts.data.api.models.posts.PostApiModel
import kz.eztech.stylyts.presentation.utils.mappers.clothes.map
import kz.eztech.stylyts.presentation.utils.mappers.comments.map
import kz.eztech.stylyts.presentation.utils.mappers.user.map
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.utils.extensions.orFalse

fun List<PostApiModel>?.map(): List<PostModel> {
    this ?: return emptyList()

    return this.map {
        PostModel(
            id = it.id ?: 0,
            description = it.description.orEmpty(),
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
        description = this?.description.orEmpty(),
        author = this?.author.map(),
        images = this?.images ?: emptyList(),
        tags = this?.tags.map(),
        hidden = this?.hidden.orFalse(),
        clothes = this?.clothes.map(),
        commentsCount = this?.commentsCount ?: 0,
        likesCount = this?.likesCount ?: 0,
        alreadyLiked = this?.alreadyLiked.orFalse(),
        firstComment = this?.firstComment.map()
    )
}