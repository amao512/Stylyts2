package kz.eztech.stylyts.constructor.presentation.presenters

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.common.data.exception.ErrorHelper
import kz.eztech.stylyts.common.presentation.base.processViewAction
import kz.eztech.stylyts.constructor.domain.models.PostModel
import kz.eztech.stylyts.constructor.domain.usecases.CreatePostUseCase
import kz.eztech.stylyts.constructor.presentation.contracts.CreateCollectionAcceptContract
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CreateCollectionAcceptPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val createPostUseCase: CreatePostUseCase
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
        description: String,
        hidden: Boolean,
        tags: String,
        file: File
    ) {
        view.displayProgress()

        val requestFile = file.asRequestBody(("image/*").toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image_one", file.name, requestFile)

        createPostUseCase.initParams(token, description, tags, body, hidden)
        createPostUseCase.execute(object : DisposableSingleObserver<PostModel>() {
            override fun onSuccess(t: PostModel) {
                view.processViewAction {
                    hideProgress()
                    processPost(postModel = t)
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