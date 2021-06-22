package kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesTypeApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun ClothesTypeApiModel?.map(): ClothesTypeModel {
    return ClothesTypeModel(
        id = this?.id ?: 0,
        title = this?.title ?: EMPTY_STRING,
        menCoverPhoto = this?.menCoverPhoto ?: EMPTY_STRING,
        womenCoverPhoto = this?.womenCoverPhoto ?: EMPTY_STRING,
        menConstructorPhoto = this?.menConstructorPhoto ?: EMPTY_STRING,
        womenConstructorPhoto = this?.womenConstructorPhoto ?: EMPTY_STRING
    )
}

fun List<ClothesTypeApiModel>?.map(): List<ClothesTypeModel> {
    this ?: return emptyList()

    return this.map {
        ClothesTypeModel(
            id = it.id ?: 0,
            title = it.title ?: EMPTY_STRING,
            menCoverPhoto = it.menCoverPhoto ?: EMPTY_STRING,
            womenCoverPhoto = it.womenCoverPhoto ?: EMPTY_STRING,
            menConstructorPhoto = it.menConstructorPhoto ?: EMPTY_STRING,
            womenConstructorPhoto = it.womenConstructorPhoto ?: EMPTY_STRING
        )
    }
}