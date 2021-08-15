package kz.eztech.stylyts.global.domain.models.posts

import kz.eztech.stylyts.global.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import java.io.File

data class PostCreateModel(
    val id: Int = 0,
    val description: String,
    val clothesList: List<ClothesModel>,
    val userList: List<UserModel>,
    val images: List<File> = emptyList(),
    val imageFile: File? = null,
    var hidden: Boolean = false
)