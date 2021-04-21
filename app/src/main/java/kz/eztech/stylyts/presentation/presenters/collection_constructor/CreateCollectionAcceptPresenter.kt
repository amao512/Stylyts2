package kz.eztech.stylyts.presentation.presenters.collection_constructor

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.outfits.OutfitCreateModel
import kz.eztech.stylyts.domain.models.outfits.OutfitModel
import kz.eztech.stylyts.domain.models.posts.PostCreateModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.posts.CreatePostUseCase
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
    private val createPostUseCase: CreatePostUseCase,
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

    override fun createPost(
        token: String,
        postCreateModel: PostCreateModel
    ) {
        view.displayProgress()

        createPostUseCase.initParams(token, postCreateModel)
        createPostUseCase.execute(object : DisposableSingleObserver<PostModel>() {
            override fun onSuccess(t: PostModel) {
                view.processViewAction {
                    hideProgress()
                    processSuccessPost(postModel = t)
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

    override fun saveCollection(token: String, model: OutfitCreateModel, data: File) {
        view.displayProgress()

        saveOutfitConstructorUseCase.initParam(
            token = token,
            data = getClothesMultipartList(model, data)
        )
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
        model: OutfitCreateModel,
        data: File
    ) {
        view.displayProgress()

        updateCollectionUseCase.initParam(
            token = token,
            id = id,
            data = getClothesMultipartList(model, data)
        )
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

    private fun getClothesMultipartList(
        model: OutfitCreateModel,
        data: File
    ): ArrayList<MultipartBody.Part> {
        val requestFile = data.asRequestBody(("image/*").toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("cover_photo", data.name, requestFile)
        val clothesList = ArrayList<MultipartBody.Part>()
        model.clothes.forEach {
            clothesList.add(MultipartBody.Part.createFormData("clothes", it.toString()))
        }

        clothesList.add(body)
        model.itemLocation.forEachIndexed { index, clothesCollection ->
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]clothes_id",
                    clothesCollection.id.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_x",
                    clothesCollection.pointX.toString()
                )
            )
            clothesList.add(
                MultipartBody.Part.createFormData(
                    "clothes_location[${index}]point_y",
                    clothesCollection.pointY.toString()
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
                model.totalPrice.toString()
            )
        )

        return clothesList
    }
}