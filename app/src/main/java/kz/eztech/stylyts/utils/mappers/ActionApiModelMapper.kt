package kz.eztech.stylyts.utils.mappers

import kz.eztech.stylyts.global.data.models.ActionApiModel
import kz.eztech.stylyts.global.domain.models.common.ActionModel
import kz.eztech.stylyts.global.presentation.common.enums.LikeEnum

fun ActionApiModel?.map(): ActionModel {
    return ActionModel(action = this?.action ?: LikeEnum.UNLIKE.title)
}