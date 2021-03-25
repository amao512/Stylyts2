package kz.eztech.stylyts.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.eztech.stylyts.presentation.adapters.collection_constructor.holders.PhotoLibraryHolder
import kz.eztech.stylyts.presentation.adapters.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    protected var currentList: MutableList<Any> = mutableListOf()

    var itemClickListener: UniversalViewClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        return getViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindData(currentList[position], position)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!payloads.isEmpty()) {
            when (payloads[0]) {
                is Int -> {
                    when (holder) {
                        is PhotoLibraryHolder -> {
                            holder.bindPayloadData(
                                currentList[position], position,
                                payloads[0] as Int
                            )
                        }
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    override fun getItemCount(): Int {
        return currentList.count()
    }

    fun updateList(list: List<Any>) {
        val diffCallback = getDiffUtilCallBack(list)

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        currentList.clear()
        currentList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateListFull(list: List<Any>) {
        currentList.clear()
        currentList.addAll(list)
        val diffCallback = getDiffUtilCallBack(list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearList() {
        currentList.clear()
        notifyDataSetChanged()
    }


    fun setOnClickListener(listener: UniversalViewClickListener) {
        itemClickListener = listener
    }

    abstract fun getLayoutId(): Int

    abstract fun getDiffUtilCallBack(list: List<Any>): BaseDiffUtilCallBack

    abstract fun getViewHolder(view: View): BaseViewHolder
}


