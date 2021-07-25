package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import java.io.File

interface CreateCollectionAcceptContract {

    interface View : BaseView {

        fun processSuccessSavingOutfit(outfitModel: OutfitCreateModel)

        fun processSuccessSavingPost(postModel: PostCreateModel)

        fun processSuccessUpdatingPost(postModel: PostCreateModel)

        fun processSuccessSavingToCart()
    }

    interface Presenter : BasePresenter<View> {

        fun createPost(postCreateModel: PostCreateModel)

        fun updatePost(
            postId: Int,
            postCreateModel: PostCreateModel
        )

        fun createOutfit(
            model: OutfitCreateModel,
            data: File
        )

        fun updateOutfit(
            id: Int,
            model: OutfitCreateModel,
            data: File
        )

        fun saveToCart(outfitCreateModel: OutfitCreateModel)
    }
}