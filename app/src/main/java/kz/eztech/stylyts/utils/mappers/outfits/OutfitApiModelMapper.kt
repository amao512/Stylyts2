package kz.eztech.stylyts.utils.mappers.outfits

import kz.eztech.stylyts.global.domain.models.outfits.OutfitApiModel
import kz.eztech.stylyts.global.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.global.presentation.common.enums.GenderEnum
import kz.eztech.stylyts.utils.extensions.getZonedDateTime
import kz.eztech.stylyts.utils.mappers.clothes.map
import kz.eztech.stylyts.utils.mappers.user.map

fun List<OutfitApiModel>?.map(): List<OutfitModel> {
    this ?: return emptyList()

    return this.map {
        OutfitModel(
            id = it.id ?: 0,
            clothes = it.clothes.map(),
            author = it.author.map(),
            title = it.title.orEmpty(),
            text = it.text.orEmpty(),
            gender = it.gender ?: GenderEnum.MALE.gender,
            totalPrice = it.totalPrice ?: 0,
            currency = it.currency.orEmpty(),
            coverPhoto = it.coverPhoto.orEmpty(),
            livePhoto = it.livePhoto.orEmpty(),
            clothesLocation = it.clothesLocation.map(),
            constructorCode = it.constructorCode.orEmpty(),
            saved = it.saved,
            createdAt = it.createdAt.orEmpty().getZonedDateTime(),
            modified_at = it.modified_at.orEmpty().getZonedDateTime(),
            style = it.style ?: 0
        )
    }
}

fun OutfitApiModel?.map(): OutfitModel {
    return OutfitModel(
        id = this?.id ?: 0,
        clothes = this?.clothes.map(),
        author = this?.author.map(),
        title = this?.title.orEmpty(),
        text = this?.text.orEmpty(),
        gender = this?.gender ?: GenderEnum.MALE.gender,
        totalPrice = this?.totalPrice ?: 0,
        currency = this?.currency.orEmpty(),
        coverPhoto = this?.coverPhoto.orEmpty(),
        livePhoto = this?.livePhoto.orEmpty(),
        clothesLocation = this?.clothesLocation.map(),
        constructorCode = this?.constructorCode.orEmpty(),
        saved = this?.saved ?: false,
        createdAt = this?.createdAt.orEmpty().getZonedDateTime(),
        modified_at = this?.modified_at.orEmpty().getZonedDateTime(),
        style = this?.style ?: 0
    )
}