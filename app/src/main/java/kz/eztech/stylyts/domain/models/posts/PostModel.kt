package kz.eztech.stylyts.domain.models.posts

data class PostModel(
    val id: Int,
    val description: String,
    val author: Int,
    val images: List<String>,
    val tags: TagsModel,
    val hidden: Boolean
)