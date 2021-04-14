package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 31.01.2021.
 */
interface CollectionDetailContract {

    interface View: BaseView {

        fun processOutfit(outfitModel: OutfitModel)

        fun processOutfitOwner(userModel: UserModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getOutfitById(
            token: String,
            outfitId: String
        )

        fun getOutfitOwner(
            token: String,
            userId: String
        )
    }
}