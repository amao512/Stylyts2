package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import java.io.File

interface CreateCollectionAcceptContract {

    interface View : BaseView {

        fun processSuccessSavingOutfit(outfitModel: OutfitModel)

        fun processSuccessSavingPost(postModel: PostModel)
    }

    interface Presenter : BasePresenter<View> {

        fun createPost(
            token: String,
            postCreateModel: PostCreateModel
        )

        fun updatePost(
            token: String,
            postId: Int,
            postCreateModel: PostCreateModel
        )

        fun createOutfit(
            token: String,
            model: OutfitCreateModel,
            data: File
        )

        fun updateOutfit(
            token: String,
            id: Int,
            model: OutfitCreateModel,
            data: File
        )
    }
}