package kz.eztech.stylyts.utils.mappers.posts

import kz.eztech.stylyts.global.data.models.posts.PostApiModel
import kz.eztech.stylyts.global.domain.models.posts.PostModel
import kz.eztech.stylyts.utils.extensions.orFalse
import kz.eztech.stylyts.utils.mappers.clothes.map
import kz.eztech.stylyts.utils.mappers.comments.map
import kz.eztech.stylyts.utils.mappers.user.map

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