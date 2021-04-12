package kz.eztech.stylyts.domain.models.user

data class FollowSuccessModel(
    val id: Int,
    val createdAt: String,
    val modifiedAt: String,
    val follower: Int,
    val followee: Int
)