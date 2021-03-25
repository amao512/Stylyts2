package kz.eztech.stylyts.collection_constructor.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.collection_constructor.domain.models.PublicationModel
import kz.eztech.stylyts.collection_constructor.domain.usecases.CreateCollectionUseCase
import kz.eztech.stylyts.collection_constructor.presentation.contracts.CreateCollectionAcceptContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CreateCollectionAcceptPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val createCollectionUseCase: CreateCollectionUseCase
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
        description: String,
        hidden: Boolean,
        tags: String,
        file: File
    ) {
        view.displayProgress()

        val requestFile = file.asRequestBody(("image/*").toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image_one", file.name, requestFile)

        createCollectionUseCase.initParams(token, description, tags, body, hidden)
        createCollectionUseCase.execute(object : DisposableSingleObserver<PublicationModel>() {
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
}