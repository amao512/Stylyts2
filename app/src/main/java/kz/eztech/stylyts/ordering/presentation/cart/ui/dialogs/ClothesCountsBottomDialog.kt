package kz.eztech.stylyts.ordering.presentation.cart.ui.dialogs

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_bottom_clothes_sizes.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.clothes.ClothesCountModel
import kz.eztech.stylyts.global.presentation.clothes.ui.adapters.CountsAdapter
import kz.eztech.stylyts.global.presentation.base.BaseRoundedBottomSheetDialog

class ClothesCountsBottomDialog : BaseRoundedBottomSheetDialog(R.layout.dialog_bottom_clothes_sizes), View.OnClickListener {

    private lateinit var titleTextView: TextView
    private lateinit var chooserListView: ListView

    private var currentItem: Any? = null

    companion object {
        const val COUNTS_KEY = "counts"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        processCounts()
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
        titleTextView.text = getString(R.string.detail_chooser_counts_table)
        chooserListView = dialog_bottom_clothes_sizes_chooser_list_view
    }

    private fun processCounts() {
        getCountsFromArgs()?.let { count ->
            titleTextView.text = getString(R.string.detail_chooser_size_table)

            if (count.isNotEmpty()) {
                val adapter = CountsAdapter(requireContext(), count)

                chooserListView.adapter = adapter
                currentItem = count[0]
                chooserListView.setOnItemClickListener { _, _, position, _ ->
                    currentItem = count[position]
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

    private fun getCountsFromArgs(): ArrayList<ClothesCountModel>? {
        return arguments?.getParcelableArrayList(COUNTS_KEY)
    }
}