package kz.eztech.stylyts.domain.models.posts

data class PostFilterModel(
    var userId: Int = 0,
    var page: Int = 1,
    var isLastPage: Boolean = false
)