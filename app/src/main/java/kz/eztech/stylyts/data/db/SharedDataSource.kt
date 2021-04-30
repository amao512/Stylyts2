package kz.eztech.stylyts.data.db

import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.presentation.base.BaseActivity
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING

object SharedDataSource {

    fun getToken(activity: BaseActivity): String {
        return activity.getSharedPrefByKey<String>(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }

    fun getUserId(activity: BaseActivity): Int {
        return activity.getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY) ?: 0
    }
}