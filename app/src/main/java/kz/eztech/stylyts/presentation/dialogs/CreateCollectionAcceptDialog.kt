package kz.eztech.stylyts.presentation.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.dialog_create_collection_accept.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.presentation.base.DialogChooserListener

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CreateCollectionAcceptDialog:DialogFragment(),View.OnClickListener{
    internal var listener: DialogChooserListener? = null
    private var currentModel: CollectionPostCreateModel? = null
    fun setChoiceListener(listener: DialogChooserListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_create_collection_accept, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(include_toolbar_dialog_create_collection){
            image_button_left_corner_action.visibility = android.view.View.GONE
            text_view_toolbar_back.visibility = android.view.View.VISIBLE
            text_view_toolbar_back.setOnClickListener {
                dismiss()
            }
            text_view_toolbar_title.visibility = android.view.View.VISIBLE
            text_view_toolbar_title.text = "Создать образ"
            text_view_toolbar_right_text.visibility = android.view.View.VISIBLE
            text_view_toolbar_right_text.text = "Готово"
            text_view_toolbar_right_text.setOnClickListener{
                currentModel?.text = edit_text_view_dialog_create_collection_accept_sign.text.toString()
                currentModel?.title =  edit_text_view_dialog_create_collection_accept_sign.text.toString()
                listener?.onChoice(it,currentModel)
                dismiss()
            }
            elevation = 0f
        }

        arguments?.let {
            if(it.containsKey("collectionModel")){
                currentModel = it.getParcelable("collectionModel")
            }
        }

        text_view_save_collection.setOnClickListener {
            currentModel?.text = edit_text_view_dialog_create_collection_accept_sign.text.toString()
            currentModel?.title =  edit_text_view_dialog_create_collection_accept_sign.text.toString()
            listener?.onChoice(it,currentModel)
            dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

    override fun onClick(v: View?) {

    }
}