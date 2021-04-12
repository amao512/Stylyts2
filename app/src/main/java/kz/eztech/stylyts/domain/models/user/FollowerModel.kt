package kz.eztech.stylyts.domain.models.user

data class FollowerModel(
    val id: Int,
    val username: String,
    val isAlreadyFollow: Boolean
)