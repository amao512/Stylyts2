package kz.eztech.stylyts.data.mappers.outfits

import kz.eztech.stylyts.data.api.models.outfits.OutfitApiModel
import kz.eztech.stylyts.data.mappers.user.UserShortApiModelMapper
import kz.eztech.stylyts.data.mappers.clothes.ClothesApiModelMapper
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.presentation.enums.GenderEnum
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import javax.inject.Inject

class OutfitApiModelMapper @Inject constructor(
    private val clothesApiModelMapper: ClothesApiModelMapper,
    private val userShortApiModelMapper: UserShortApiModelMapper,
    private val clothesLocationApiModelMapper: ClothesLocationApiModelMapper
) {

    fun map(data: List<OutfitApiModel>?): List<OutfitModel> {
        data ?: return emptyList()

        return data.map {
            OutfitModel(
                id = it.id ?: 0,
                clothes = clothesApiModelMapper.map(it.clothes),
                author = userShortApiModelMapper.map(it.author),
                title = it.title ?: EMPTY_STRING,
                text = it.text ?: EMPTY_STRING,
                gender = it.gender ?: GenderEnum.MALE.gender,
                totalPrice = it.totalPrice ?: 0,
                currency = it.currency ?: EMPTY_STRING,
                coverPhoto = it.coverPhoto ?: EMPTY_STRING,
                livePhoto = it.livePhoto ?: EMPTY_STRING,
                clothesLocation = clothesLocationApiModelMapper.map(it.clothesLocation),
                constructorCode = it.constructorCode ?: EMPTY_STRING,
                saved = it.saved,
                createdAt = it.createdAt ?: EMPTY_STRING,
                modified_at = it.modified_at ?: EMPTY_STRING,
                style = it.style ?: 0
            )
        }
    }

    fun map(data: OutfitApiModel?): OutfitModel {
        return OutfitModel(
            id = data?.id ?: 0,
            clothes = clothesApiModelMapper.map(data?.clothes),
            author = userShortApiModelMapper.map(data?.author),
            title = data?.title ?: EMPTY_STRING,
            text = data?.text ?: EMPTY_STRING,
            gender = data?.gender ?: GenderEnum.MALE.gender,
            totalPrice = data?.totalPrice ?: 0,
            currency = data?.currency ?: EMPTY_STRING,
            coverPhoto = data?.coverPhoto ?: EMPTY_STRING,
            livePhoto = data?.livePhoto ?: EMPTY_STRING,
            clothesLocation = clothesLocationApiModelMapper.map(data?.clothesLocation),
            constructorCode = data?.constructorCode ?: EMPTY_STRING,
            saved = data?.saved ?: false,
            createdAt = data?.createdAt ?: EMPTY_STRING,
            modified_at = data?.modified_at ?: EMPTY_STRING,
            style = data?.style ?: 0
        )
    }
}