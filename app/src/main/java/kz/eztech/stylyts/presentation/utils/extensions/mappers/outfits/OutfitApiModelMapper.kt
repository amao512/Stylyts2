package kz.eztech.stylyts.presentation.utils.extensions.mappers.outfits

import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import kz.eztech.stylyts.presentation.utils.extensions.mappers.clothes.map
import kz.eztech.stylyts.presentation.utils.extensions.mappers.user.map
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

fun List<OutfitApiModel>?.map(): List<OutfitModel> {
    this ?: return emptyList()

    return this.map {
        OutfitModel(
            id = it.id ?: 0,
            clothes = it.clothes.map(),
            author = it.author.map(),
            title = it.title ?: EMPTY_STRING,
            text = it.text ?: EMPTY_STRING,
            gender = it.gender ?: GenderEnum.MALE.gender,
            totalPrice = it.totalPrice ?: 0,
            currency = it.currency ?: EMPTY_STRING,
            coverPhoto = it.coverPhoto ?: EMPTY_STRING,
            livePhoto = it.livePhoto ?: EMPTY_STRING,
            clothesLocation = it.clothesLocation.map(),
            constructorCode = it.constructorCode ?: EMPTY_STRING,
            saved = it.saved,
            createdAt = it.createdAt ?: EMPTY_STRING,
            modified_at = it.modified_at ?: EMPTY_STRING,
            style = it.style ?: 0
        )
    }
}

fun OutfitApiModel?.map(): OutfitModel {
    return OutfitModel(
        id = this?.id ?: 0,
        clothes = this?.clothes.map(),
        author = this?.author.map(),
        title = this?.title ?: EMPTY_STRING,
        text = this?.text ?: EMPTY_STRING,
        gender = this?.gender ?: GenderEnum.MALE.gender,
        totalPrice = this?.totalPrice ?: 0,
        currency = this?.currency ?: EMPTY_STRING,
        coverPhoto = this?.coverPhoto ?: EMPTY_STRING,
        livePhoto = this?.livePhoto ?: EMPTY_STRING,
        clothesLocation = this?.clothesLocation.map(),
        constructorCode = this?.constructorCode ?: EMPTY_STRING,
        saved = this?.saved ?: false,
        createdAt = this?.createdAt ?: EMPTY_STRING,
        modified_at = this?.modified_at ?: EMPTY_STRING,
        style = this?.style ?: 0
    )
}