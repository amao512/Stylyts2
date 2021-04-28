package kz.eztech.stylyts.domain.models.user

data class FollowerModel(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val avatar: String,
    var isAlreadyFollow: Boolean
)