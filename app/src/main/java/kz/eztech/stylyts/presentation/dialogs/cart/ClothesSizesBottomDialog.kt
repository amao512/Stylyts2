package kz.eztech.stylyts.presentation.dialogs.cart

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_bottom_clothes_sizes.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.clothes.ClothesSizeModel
import kz.eztech.stylyts.presentation.adapters.clothes.SizeAdapter
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        processSizes()
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
                val adapter = SizeAdapter(requireContext(), sizeModel)

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

    private fun initializeListeners() {
        dialog_bottom_clothes_sizes_done_text_view.setOnClickListener(this)
    }

    private fun getSizesFromArgs(): ArrayList<ClothesSizeModel>? {
        return arguments?.getParcelableArrayList(SIZES_KEY)
    }
}