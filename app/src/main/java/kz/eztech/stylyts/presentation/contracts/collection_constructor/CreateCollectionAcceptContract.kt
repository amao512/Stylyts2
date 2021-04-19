package kz.eztech.stylyts.presentation.contracts.collection_constructor

import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.presentation.base.BasePresenter
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.user.UserModel
import java.io.File

interface CreateCollectionAcceptContract {

    interface View : BaseView {

        fun processPublications(publicationModel: PublicationModel)

        fun processSuccessSaving(outfitModel: OutfitModel?)
    }

    interface Presenter : BasePresenter<View> {

        fun createPublications(
            token: String,
            selectedClothes: ArrayList<ClothesModel>?,
            selectedUsers: ArrayList<UserModel>?,
            description: String,
            file: File
        )

        fun saveCollection(
            token: String,
            model: CollectionPostCreateModel,
            data: File
        )

        fun updateCollection(
            token: String,
            id: Int,
            model: CollectionPostCreateModel,
            data: File
        )
    }
}