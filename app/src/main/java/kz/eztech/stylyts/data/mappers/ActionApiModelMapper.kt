package kz.eztech.stylyts.data.mappers

import kz.eztech.stylyts.data.api.models.ActionApiModel
import kz.eztech.stylyts.domain.models.ActionModel
import kz.eztech.stylyts.presentation.enums.LikeEnum
import javax.inject.Inject

class ActionApiModelMapper @Inject constructor() {

    fun map(data: ActionApiModel?): ActionModel {
        return ActionModel(
            action = data?.action ?: LikeEnum.UNLIKE.title
        )
    }
}