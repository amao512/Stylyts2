package kz.eztech.stylyts.utils.mappers.clothes

import kz.eztech.stylyts.data.api.models.clothes.ClothesApiModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.utils.mappers.user.map

fun List<ClothesApiModel>?.map(): List<ClothesModel> {
    this ?: return emptyList()

    return this.map {
        ClothesModel(
            id = it.id ?: 0,
            clothesStyle = it.clothesStyle.map(),
            clothesCategory = it.clothesCategory.map(),
            constructorImage = it.constructorImage.orEmpty(),
            coverImages = it.coverImages ?: emptyList(),
            sizeInStock = it.sizeInStock.map(),
            clothesColor = it.clothesColor.orEmpty(),
            title = it.title.orEmpty(),
            description = it.description.orEmpty(),
            gender = it.gender ?: GenderEnum.MALE.gender,
            cost = it.cost ?: 0,
            salePrice = it.salePrice ?: 0,
            currency = it.currency.orEmpty(),
            productCode = it.productCode.orEmpty(),
            createdAt = it.createdAt.orEmpty(),
            modifiedAt = it.modifiedAt.orEmpty(),
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
        constructorImage = this?.constructorImage.orEmpty(),
        coverImages = this?.coverImages ?: emptyList(),
        sizeInStock = this?.sizeInStock.map(),
        clothesColor = this?.clothesColor.orEmpty(),
        title = this?.title.orEmpty(),
        description = this?.description.orEmpty(),
        gender = this?.gender ?: GenderEnum.MALE.gender,
        cost = this?.cost ?: 0,
        salePrice = this?.salePrice ?: 0,
        currency = this?.currency.orEmpty(),
        productCode = this?.productCode.orEmpty(),
        createdAt = this?.createdAt.orEmpty(),
        modifiedAt = this?.modifiedAt.orEmpty(),
        owner = this?.owner.map(),
        clothesBrand = this?.clothesBrand.map(),
    )
}