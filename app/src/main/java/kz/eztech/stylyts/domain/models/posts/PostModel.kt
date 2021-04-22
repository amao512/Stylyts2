package kz.eztech.stylyts.domain.models.posts

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel

data class PostModel(
    val id: Int,
    val description: String,
    val author: Int,
    val images: List<String>,
    val tags: TagsModel,
    val hidden: Boolean,
    val clothes: List<ClothesModel>,

    var owner: UserModel? = null,
)