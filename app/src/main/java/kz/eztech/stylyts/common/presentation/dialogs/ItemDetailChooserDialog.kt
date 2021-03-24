package kz.eztech.stylyts.common.presentation.dialogs

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.dialog_bottom_item_detail_chooser.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.ClothesColor
import kz.eztech.stylyts.common.domain.models.ClothesSize
import kz.eztech.stylyts.common.presentation.base.BaseRoundedBottomSheetDialog

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ItemDetailChooserDialog : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_item_detail_chooser),View.OnClickListener {
    private var currentSize: List<ClothesSize>? = null
    private var currentColor: List<ClothesColor>? = null
    private var currentItem:Any? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if(it.containsKey("sizeItems")){
                currentSize = it.getParcelableArrayList<ClothesSize>("sizeItems")
                currentSize?.let { sizeModel ->
                    if(sizeModel.isNotEmpty()){
                        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                requireContext(), R.layout.item_style, sizeModel.map { it.size }
                        )
                        list_view_dialog_bottom_item_detail_chooser_list.adapter = adapter
                        currentItem = sizeModel[0]
                        list_view_dialog_bottom_item_detail_chooser_list.setOnItemClickListener { parent, view, position, id ->
        
                            currentItem = sizeModel[position]
                            listener?.onChoice(list_view_dialog_bottom_item_detail_chooser_list,currentItem)
                            dismiss()
                        }
                    }else{
                       dismiss()
                    }
                }
            }
            if (it.containsKey("colorItems")){
                currentColor = it.getParcelableArrayList<ClothesColor>("colorItems")
                currentColor?.let { colorModel ->
                    if(colorModel.isNotEmpty()){
                        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                requireContext(), R.layout.item_style, colorModel.map { it.color }
                        )
                        list_view_dialog_bottom_item_detail_chooser_list.adapter = adapter
                        currentItem = colorModel[0]
                        list_view_dialog_bottom_item_detail_chooser_list.setOnItemClickListener { parent, view, position, id ->
                            currentItem = colorModel[position]
                            listener?.onChoice(list_view_dialog_bottom_item_detail_chooser_list,currentItem)
                            dismiss()
                        }
                    }else{
                        dismiss()
                    }
                   
                }
            }


        }


        initializeListeners()
    }

    private fun initializeListeners(){
        text_view_dialog_bottom_item_detail_chooser_done.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.text_view_dialog_bottom_item_detail_chooser_done -> {
                listener?.onChoice(v,currentItem)
                dismiss()
            }
        }
    }
}