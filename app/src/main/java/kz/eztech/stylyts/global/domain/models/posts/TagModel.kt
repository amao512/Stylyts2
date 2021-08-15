package kz.eztech.stylyts.global.domain.models.posts

data class TagModel(
    val id: Int,
    val title: String,
    val pointX: Double,
    val pointY: Double
) {
    var referralUser: Int = 0
}