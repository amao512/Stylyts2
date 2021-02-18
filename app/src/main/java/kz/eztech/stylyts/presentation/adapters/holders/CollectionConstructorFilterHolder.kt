package kz.eztech.stylyts.presentation.adapters.holders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.item_constructor_filter_clothe_items.view.*
import kotlinx.android.synthetic.main.item_main_image.view.*
import kz.eztech.stylyts.domain.models.BrandModel
import kz.eztech.stylyts.domain.models.ClothesTypes
import kz.eztech.stylyts.domain.models.GenderCategory
import kz.eztech.stylyts.presentation.adapters.CollectionConstructorSubFilterAdapter
import kz.eztech.stylyts.presentation.adapters.MainImagesAdditionalAdapter
import kz.eztech.stylyts.presentation.adapters.base.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.holders.base.BaseViewHolder
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener

/**
 * Created by Ruslan Erdenoff on 05.02.2021.
 */
class CollectionConstructorFilterHolder(itemView: View, adapter: BaseAdapter): BaseViewHolder(itemView,adapter) {
   
    override fun bindData(item: Any, position: Int) {
        with(itemView){
            when(item){
                is BrandModel -> {
                    frame_layout_tem_constructor_filter_clothe_container.setOnClickListener {
                        adapter.itemClickListener?.onViewClicked(it,position,item)
                    }
                    text_view_item_constructor_filter_clothe_item_name.text = item.first_name
                    if(item.isChosen){
                        image_view_item_constructor_filter_clothe_items.visibility =View.VISIBLE
                    }else{
                        image_view_item_constructor_filter_clothe_items.visibility =View.GONE
                    }
                    recycler_view_item_constructor_filter_clothe_items.visibility = View.VISIBLE
                }
                is GenderCategory -> {
                    recycler_view_item_constructor_filter_clothe_items.visibility = View.GONE
                    var filterMap = HashMap<String,Any>()
                    val currentIds = ArrayList<Int>()
                    var clothesTypes = ArrayList<ClothesTypes>()
                    var additionalAdapter = CollectionConstructorSubFilterAdapter()
                    text_view_item_constructor_filter_clothe_item_name.text = item.title
                    item.clothes_types?.let {
                        clothesTypes.clear()
                        clothesTypes.addAll(it)
                        additionalAdapter.itemClickListener = object : UniversalViewClickListener{
                            override fun onViewClicked(view: View, position: Int, subItem: Any?) {
                                when(subItem){
                                    is ClothesTypes -> {
                                        val model = clothesTypes.get(clothesTypes.indexOf(subItem))
                                        
                                        if(currentIds.contains(subItem.id)){
                                            model.isChosen = false
                                            currentIds.remove(subItem.id)
                                        }else{
                                            model.isChosen = true
                                            currentIds.add(subItem.id!!)
                                        }
                                        item.chosenClothesTypes = subItem.id
                                        adapter.itemClickListener?.onViewClicked(frame_layout_tem_constructor_filter_clothe_container,position,item)
                                        additionalAdapter.updateList(clothesTypes)
                                        additionalAdapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }
                        this.recycler_view_item_constructor_filter_clothe_items.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
                        this.recycler_view_item_constructor_filter_clothe_items.adapter = additionalAdapter
                        recycler_view_item_constructor_filter_clothe_items.visibility = View.VISIBLE
                        additionalAdapter.updateList(clothesTypes)
                        additionalAdapter.notifyDataSetChanged()
    
                        frame_layout_tem_constructor_filter_clothe_container.setOnClickListener {
                            
                        }
                    }
                   
                }
                else -> {}
            }
        }
    }
}