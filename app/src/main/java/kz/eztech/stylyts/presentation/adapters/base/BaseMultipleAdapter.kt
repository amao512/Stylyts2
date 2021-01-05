package kz.eztech.stylyts.presentation.adapters.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.eztech.stylyts.presentation.adapters.holders.CollectionFilterHolder
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import java.lang.reflect.Constructor


/**
 * Created by Ruslan Erdenoff on 25.11.2020.
 */
abstract class BaseMultipleAdapter: RecyclerView.Adapter<BaseViewHolder>(){
    protected var currentList: ArrayList<Any>

    private var itemClickListener: UniversalViewClickListener? = null
    init {
        currentList = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(getLayoutId(), parent, false)
        //return getViewHolder(view)
        return getViewHolder(parent,viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindData(currentList[position], position)
    }

    override fun getItemCount(): Int {
        return currentList.count()
    }

    fun updateList(list: List<Any>){
        val diffCallback = getDiffUtilCallBack(list)

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        currentList.clear()
        currentList.addAll(list)
        diffResult.dispatchUpdatesTo(this)


    }

    fun setOnClickListener(listener: UniversalViewClickListener){
        itemClickListener = listener
    }

    abstract fun getDiffUtilCallBack(list: List<Any>):BaseDiffUtilCallBack

    abstract fun getViewHolder(parent: ViewGroup, viewType: Int):BaseViewHolder
    
    /*
    fun <VH:BaseViewHolder> createItem(
        viewGroup: ViewGroup,
        layoutRes: Int,
        method: (View) -> VH
    ): VH {
        val view = LayoutInflater.from(viewGroup.context).inflate(layoutRes, viewGroup, false)
        return method(view)
    }*/
}


