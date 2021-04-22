package kz.eztech.stylyts.presentation.contracts.collection

import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.models.posts.TagModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView

/**
 * Created by Ruslan Erdenoff on 31.01.2021.
 */
interface CollectionDetailContract {

    interface View: BaseView {

        fun processOutfit(outfitModel: OutfitModel)

        fun processOwner(userModel: UserModel)

        fun processPost(postModel: PostModel)
    }

    interface Presenter: BasePresenter<View> {

        fun getOutfitById(
            token: String,
            outfitId: String
        )

        fun getPostById(
            token: String,
            postId: Int
        )

        fun getOwner(
            token: String,
            userId: String
        )
    }
}