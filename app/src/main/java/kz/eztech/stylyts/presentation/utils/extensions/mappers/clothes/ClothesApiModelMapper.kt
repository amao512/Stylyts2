package kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesApiModel
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<ClothesApiModel>?.map(): List<ClothesModel> {
    this ?: return emptyList()

    return this.map {
        ClothesModel(
            id = it.id ?: 0,
            clothesStyle = it.clothesStyle.map(),
            clothesCategory = it.clothesCategory.map(),
            constructorImage = it.constructorImage ?: EMPTY_STRING,
            coverImages = it.coverImages ?: emptyList(),
            sizeInStock = it.sizeInStock.map(),
            clothesColor = it.clothesColor ?: EMPTY_STRING,
            title = it.title ?: EMPTY_STRING,
            description = it.description ?: EMPTY_STRING,
            gender = it.gender ?: GenderEnum.MALE.gender,
            cost = it.cost ?: 0,
            salePrice = it.salePrice ?: 0,
            currency = it.currency ?: EMPTY_STRING,
            productCode = it.productCode ?: EMPTY_STRING,
            createdAt = it.createdAt ?: EMPTY_STRING,
            modifiedAt = it.modifiedAt ?: EMPTY_STRING,
            owner = it.owner.map(),
            clothesBrand = it.clothesBrand.map(),
        )
    }
}

fun ClothesApiModel?.map(): ClothesModel {
    return ClothesModel(
        id = this?.id ?: 0,
        clothesStyle = this?.clothesStyle.map(),
        clothesCategory = this?.clothesCategory.map(),
        constructorImage = this?.constructorImage ?: EMPTY_STRING,
        coverImages = this?.coverImages ?: emptyList(),
        sizeInStock = this?.sizeInStock.map(),
        clothesColor = this?.clothesColor ?: EMPTY_STRING,
        title = this?.title ?: EMPTY_STRING,
        description = this?.description ?: EMPTY_STRING,
        gender = this?.gender ?: GenderEnum.MALE.gender,
        cost = this?.cost ?: 0,
        salePrice = this?.salePrice ?: 0,
        currency = this?.currency ?: EMPTY_STRING,
        productCode = this?.productCode ?: EMPTY_STRING,
        createdAt = this?.createdAt ?: EMPTY_STRING,
        modifiedAt = this?.modifiedAt ?: EMPTY_STRING,
        owner = this?.owner.map(),
        clothesBrand = this?.clothesBrand.map(),
    )
}