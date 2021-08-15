package kz.eztech.stylyts.global.domain.models.posts

data class PostFilterModel(
    var userId: Int = 0,
    var page: Int = 1,
    var isLastPage: Boolean = false
)