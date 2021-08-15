package kz.eztech.stylyts.profile.domain.models.wardrobe

data class WardrobeModel(
    val id: Int,
    val createdAt: String,
    val modifiedAt: String,
    val user: Int,
    val clothes: Int
)
