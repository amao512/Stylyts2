package kz.eztech.stylyts.presentation.adapters

import android.view.View
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.MainResult
import kz.eztech.stylyts.common.presentation.adapters.BaseAdapter
import kz.eztech.stylyts.common.presentation.adapters.BaseDiffUtilCallBack
import kz.eztech.stylyts.presentation.adapters.holders.MainImageHolder

/**
 * Created by Ruslan Erdenoff on 20.11.2020.
 */
class MainImagesAdapter : BaseAdapter(){

    override fun getLayoutId(): Int {
        return R.layout.item_main_image
    }

    override fun getViewHolder(view: View): MainImageHolder {
        return MainImageHolder(view,this)
    }

    override fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack {
        return object : BaseDiffUtilCallBack(currentList, list){
            override fun getAreContentsTheSame(
                oldItemPosition: Int,
                newItemPosition: Int
            ): Boolean {
                return (currentList[oldItemPosition] as MainResult).id ==
                        (list[newItemPosition] as MainResult).id
            }
        }
    }
}