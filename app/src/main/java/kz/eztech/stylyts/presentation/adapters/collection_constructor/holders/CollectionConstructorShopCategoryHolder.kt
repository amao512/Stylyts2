package kz.eztech.stylyts.presentation.adapters.collection_constructor.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.item_collection_constructor_category.view.*
import kotlinx.android.synthetic.main.item_collection_constructor_category_wide.view.*
import kz.eztech.stylyts.domain.models.clothes.ClothesTypeModel
import kz.eztech.stylyts.presentation.adapters.common.BaseAdapter
import kz.eztech.stylyts.presentation.adapters.common.holders.BaseViewHolder
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.loadImage
import kz.eztech.stylyts.presentation.utils.extensions.show

/**
 * Created by Ruslan Erdenoff on 25.12.2020.
 */
class CollectionConstructorShopCategoryHolder(
    itemView: View,
    adapter: BaseAdapter,
    private val gender: Int
) : BaseViewHolder(itemView, adapter) {

	private lateinit var rootViewConstraintLayout: ConstraintLayout
	private lateinit var titleTextView: TextView
	private lateinit var iconImageView: ImageView
	private lateinit var selectedIconImageView: ImageView

    override fun bindData(item: Any, position: Int) {
		item as ClothesTypeModel

		initializeViews(item)
		processClothesType(
			clothesType = item,
			position = position
		)
    }

	private fun initializeViews(clothesTypeModel: ClothesTypeModel) {
		with (itemView) {
			if (clothesTypeModel.isWiden) {
				rootViewConstraintLayout = item_collection_constructor_category_wide_root_view
				titleTextView = text_view_item_collection_constructor_category_widen_item_title
				iconImageView = image_view_item_collection_constructor_category_widen_item_image_holder
				selectedIconImageView = image_view_item_collection_constructor_category_widen_item_image_chooser
			} else {
				rootViewConstraintLayout = item_collection_constructor_category_root_view
				titleTextView = text_view_item_collection_constructor_category_item_title
				iconImageView = image_view_item_collection_constructor_category_item_image_holder
				selectedIconImageView = image_view_item_collection_constructor_category_item_image_chooser
			}
		}
	}

	private fun processClothesType(
		clothesType: ClothesTypeModel,
		position: Int
	) {
		with(clothesType) {
			if (isExternal) {
				iconImageView.setImageResource(externalImageId)
				titleTextView.text = title
			} else {
				fillCoverPhoto(clothesType)

				if (title.length > 6) {
					(title.substring(0, 7) + "...").also { titleTextView.text = it }
				} else {
					titleTextView.text = title
				}

				if (isChoosen) {
					selectedIconImageView.show()
				} else {
					selectedIconImageView.hide()
				}
			}

			rootViewConstraintLayout.setOnClickListener {
				adapter.itemClickListener?.onViewClicked(it, position, clothesType)
			}
		}
	}

	private fun fillCoverPhoto(clothesType: ClothesTypeModel) {
		val image = when (gender) {
			0 -> clothesType.menConstructorPhoto
			else -> clothesType.womenConstructorPhoto
		}

		image.loadImage(target = iconImageView)
	}
}