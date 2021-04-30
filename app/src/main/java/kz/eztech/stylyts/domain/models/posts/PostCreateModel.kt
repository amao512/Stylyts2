package kz.eztech.stylyts.domain.models.posts

import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.user.UserModel
import java.io.File

data class PostCreateModel(
    val description: String,
    val clothesList: List<ClothesModel>,
    val userList: List<UserModel>,
    val images: List<File>,
    val imageFile: File,
    var hidden: Boolean = false
)