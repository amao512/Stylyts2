package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import java.io.File

interface CreateCollectionAcceptContract {

    interface View : BaseView {

        fun processPublications(publicationModel: PublicationModel)

        fun processSuccessSaving(outfitModel: OutfitModel?)
    }

    interface Presenter : BasePresenter<View> {

        fun createPost(
            token: String,
            postCreateModel: PostCreateModel
        )

        fun saveCollection(
            token: String,
            model: OutfitCreateModel,
            data: File
        )

        fun updateCollection(
            token: String,
            id: Int,
            model: OutfitCreateModel,
            data: File
        )
    }
}