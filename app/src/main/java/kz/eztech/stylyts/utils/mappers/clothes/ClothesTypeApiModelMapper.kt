package kz.eztech.stylyts.utils.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesTypeApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel

fun ClothesTypeApiModel?.map(): ClothesTypeModel {
    return ClothesTypeModel(
        id = this?.id ?: 0,
        title = this?.title.orEmpty(),
        menCoverPhoto = this?.menCoverPhoto.orEmpty(),
        womenCoverPhoto = this?.womenCoverPhoto.orEmpty(),
        menConstructorPhoto = this?.menConstructorPhoto.orEmpty(),
        womenConstructorPhoto = this?.womenConstructorPhoto.orEmpty()
    )
}

fun List<ClothesTypeApiModel>?.map(): List<ClothesTypeModel> {
    this ?: return emptyList()

    return this.map {
        ClothesTypeModel(
            id = it.id ?: 0,
            title = it.title.orEmpty(),
            menCoverPhoto = it.menCoverPhoto.orEmpty(),
            womenCoverPhoto = it.womenCoverPhoto.orEmpty(),
            menConstructorPhoto = it.menConstructorPhoto.orEmpty(),
            womenConstructorPhoto = it.womenConstructorPhoto.orEmpty()
        )
    }
}