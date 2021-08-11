package kz.eztech.stylyts.presentation.presenters.collection

import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.posts.PostModel
import kz.eztech.stylyts.domain.usecases.posts.GetPostsUseCase
import kz.eztech.stylyts.presentation.contracts.collection.CollectionItemContract
import kz.eztech.stylyts.utils.Paginator
import javax.inject.Inject

/**
 * Created by Ruslan Erdenoff on 01.02.2021.
 */
class CollectionsItemPresenter @Inject constructor(
	private val paginator: Paginator.Store<PostModel>,
	private val getPostsUseCase: GetPostsUseCase
) : CollectionItemContract.Presenter, CoroutineScope by CoroutineScope(Dispatchers.Main) {

	private lateinit var view: CollectionItemContract.View

	init {
	    launch {
			paginator.render = { view.renderPaginatorState(it) }

			paginator.sideEffects.consumeEach { effect ->
				when (effect) {
					is Paginator.SideEffect.LoadPage -> loadPage(effect.currentPage)
					is Paginator.SideEffect.ErrorEvent -> {}
				}
			}
		}
	}

	override fun disposeRequests() {
		getPostsUseCase.clear()
		cancel()
	}

	override fun attach(view: CollectionItemContract.View) {
		this.view = view
	}

	override fun loadPage(page: Int) {
		getPostsUseCase.initParams(page = page)
		getPostsUseCase.execute(object : DisposableSingleObserver<ResultsModel<PostModel>>() {
			override fun onSuccess(t: ResultsModel<PostModel>) {
				paginator.proceed(Paginator.Action.NewPage(
					pageNumber = t.page,
					items = t.results
				))
			}

			override fun onError(e: Throwable) {
				paginator.proceed(Paginator.Action.PageError(e))
			}
		})
	}

	override fun getPosts() {
		paginator.proceed(Paginator.Action.Refresh)
	}

	override fun loadMorePost() {
		paginator.proceed(Paginator.Action.LoadMore)
	}
}