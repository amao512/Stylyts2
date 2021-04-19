package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.CollectionPostCreateModel
import kz.eztech.stylyts.domain.models.PublicationModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.collection_constructor.CreatePublicationUseCase
import kz.eztech.stylyts.domain.usecases.collection_constructor.UpdateCollectionUseCase
import kz.eztech.stylyts.domain.usecases.shop.SaveOutfitConstructorUseCase
import kz.eztech.stylyts.presentation.base.processViewAction
import kz.eztech.stylyts.presentation.contracts.collection_constructor.CreateCollectionAcceptContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CreateCollectionAcceptPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val createPublicationUseCase: CreatePublicationUseCase,
    private val saveOutfitConstructorUseCase: SaveOutfitConstructorUseCase,
    private val updateCollectionUseCase: UpdateCollectionUseCase,
) : CreateCollectionAcceptContract.Presenter {

    private lateinit var view: CreateCollectionAcceptContract.View

    override fun disposeRequests() {
        view.disposeRequests()
    }

    override fun attach(view: CreateCollectionAcceptContract.View) {
        this.view = view
    }

    override fun createPublications(
        token: String,
        selectedClothes: ArrayList<ClothesModel>?,
        selectedUsers: ArrayList<UserModel>?,
        description: String,
        file: File
    ) {
        view.displayProgress()

        val requestFile = file.asRequestBody(("image/*").toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image_one", file.name, requestFile)

        val bodyList = ArrayList<MultipartBody.Part>()

        bodyList.add(body)
        selectedClothes?.forEachIndexed { index, clothesModel ->
            bodyList.add(
                MultipartBody.Part.createFormData(
                    "tags[${index}]lol",
                    clothesModel.id.toString()
                )
            )
        }

        bodyList.add(MultipartBody.Part.createFormData("description", description))
        bodyList.add(MultipartBody.Part.createFormData("tags", ""))

        createPublicationUseCase.initParams(token, bodyList)
        createPublicationUseCase.execute(object : DisposableSingleObserver<PublicationModel>() {
            override fun onSuccess(t: PublicationModel) {
                view.processViewAction {
                    hideProgress()
                    processPublications(publicationModel = t)
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    hideProgress()
                    errorHelper.processError(e)
                }
            }
        })
    }

    override fun saveCollection(token: String, model: CollectionPostCreateModel, data: File) {
        view.displayProgress()

        val requestFile = data.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("cover_photo", data.name, requestFile)
        val clothesList = ArrayList<MultipartBody.Part>()
        model.clothes?.forEach {
            clothesList.add(MultipartBody.Part.createFormData("clothes", it.toString()))
        }

        clothesList.add(body)
        model.clothes_location?.forEachIndexed { index, clothesCollection ->
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]clothes_id",
                    clothesCollection.clothes_id.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_x",
                    clothesCollection.point_x.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_y",
                    clothesCollection.point_y.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]width",
                    clothesCollection.width.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]height",
                    clothesCollection.height.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]degree",
                    clothesCollection.degree.toString()
                )
            )
        }
        clothesList.add(MultipartBody.Part.createFormData("title", model.title.toString()))
        clothesList.add(MultipartBody.Part.createFormData("text", model.text.toString()))
        clothesList.add(MultipartBody.Part.createFormData("style", model.style.toString()))
        clothesList.add(MultipartBody.Part.createFormData("author", model.author.toString()))
        clothesList.add(
            MultipartBody.Part.createFormData(
                "total_price",
                model.total_price.toString()
            )
        )

        saveOutfitConstructorUseCase.initParam(token, clothesList)
        saveOutfitConstructorUseCase.execute(object : DisposableSingleObserver<OutfitModel>() {
            override fun onSuccess(t: OutfitModel) {
                view.processViewAction {
                    view.processSuccessSaving(outfitModel = t)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    view.processSuccessSaving(outfitModel = null)
                    hideProgress()
                }
            }
        })
    }

    override fun updateCollection(
        token: String,
        id: Int,
        model: CollectionPostCreateModel,
        data: File
    ) {
        view.displayProgress()
        val requestFile = data.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("cover_photo", data.name, requestFile)
        val clothesList = ArrayList<MultipartBody.Part>()
        model.clothes?.forEach {
            clothesList.add(MultipartBody.Part.createFormData("clothes", it.toString()))
        }

        clothesList.add(body)
        model.clothes_location?.forEachIndexed { index, clothesCollection ->
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]clothes_id",
                    clothesCollection.clothes_id.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_x",
                    clothesCollection.point_x.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_y",
                    clothesCollection.point_y.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]width",
                    clothesCollection.width.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]height",
                    clothesCollection.height.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]degree",
                    clothesCollection.degree.toString()
                )
            )
        }
        clothesList.add(MultipartBody.Part.createFormData("title", model.title.toString()))
        clothesList.add(MultipartBody.Part.createFormData("text", model.text.toString()))
        clothesList.add(MultipartBody.Part.createFormData("style", model.style.toString()))
        clothesList.add(MultipartBody.Part.createFormData("author", model.author.toString()))
        clothesList.add(
            MultipartBody.Part.createFormData(
                "total_price",
                model.total_price.toString()
            )
        )

        updateCollectionUseCase.initParam(token, id, clothesList)
        updateCollectionUseCase.execute(object : DisposableSingleObserver<Unit>() {
            override fun onSuccess(t: Unit) {
                view.processViewAction {
                    view.processSuccessSaving(null)
                    hideProgress()
                }
            }

            override fun onError(e: Throwable) {
                view.processViewAction {
                    displayMessage(errorHelper.processError(e))
                    hideProgress()
                }
            }
        })
    }
}