package kz.eztech.stylyts.presentation.dialogs

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_bottom_clothes_sizes.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.ClothesColor
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.presentation.base.BaseRoundedBottomSheetDialog

/**
 * Created by Ruslan Erdenoff on 28.01.2021.
 */
class ClothesSizesBottomDialog : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_clothes_sizes), View.OnClickListener {

    private lateinit var titleTextView: TextView
    private lateinit var chooserListView: ListView

    private var currentItem: Any? = null

    companion object {
        const val SIZES_KEY = "sizes"
        const val COLORS_KEY = "colors"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        processSizes()
        processColors()
        initializeListeners()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_bottom_clothes_sizes_done_text_view -> {
                listener?.onChoice(v, currentItem)
                dismiss()
            }
        }
    }

    private fun initializeViews() {
        titleTextView = dialog_bottom_clothes_sizes_title_text_view
        chooserListView = dialog_bottom_clothes_sizes_chooser_list_view
    }

    private fun processSizes() {
        getSizesFromArgs()?.let { sizeModel ->
            titleTextView.text = getString(R.string.detail_chooser_size_table)

            if (sizeModel.isNotEmpty()) {
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(), R.layout.item_style, sizeModel.map { it.size }
                )
                chooserListView.adapter = adapter
                currentItem = sizeModel[0]
                chooserListView.setOnItemClickListener { _, _, position, _ ->
                    currentItem = sizeModel[position]
                    listener?.onChoice(chooserListView, currentItem)

                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }

    private fun processColors() {
        getColorsFromArgs()?.let { colorModel ->
            titleTextView.text = getString(R.string.detail_chooser_color_table)

            if (colorModel.isNotEmpty()) {
                val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    requireContext(), R.layout.item_style, colorModel.map { it.color }
                )
                chooserListView.adapter = adapter
                currentItem = colorModel[0]
                chooserListView.setOnItemClickListener { _, _, position, _ ->
                    currentItem = colorModel[position]
                    listener?.onChoice(chooserListView, currentItem)

                    dismiss()
                }
            } else {
                dismiss()
            }
        }
    }

    private fun initializeListeners() {
        dialog_bottom_clothes_sizes_done_text_view.setOnClickListener(this)
    }

    private fun getSizesFromArgs(): ArrayList<ClothesSizeModel>? {
        return arguments?.getParcelableArrayList(SIZES_KEY)
    }

    private fun getColorsFromArgs(): ArrayList<ClothesColor>? {
        return arguments?.getParcelableArrayList(COLORS_KEY)
    }
}