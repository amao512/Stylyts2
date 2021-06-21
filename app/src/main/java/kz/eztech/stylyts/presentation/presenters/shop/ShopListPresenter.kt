package kz.eztech.stylyts.presentation.presenters.shop

import io.reactivex.observers.DisposableSingleObserver
import kz.eztech.stylyts.data.exception.ErrorHelper
import kz.eztech.stylyts.domain.models.common.ResultsModel
import kz.eztech.stylyts.domain.models.shop.ShopListItem
import kz.eztech.stylyts.domain.models.user.UserModel
import kz.eztech.stylyts.domain.usecases.search.SearchProfileUseCase
import kz.eztech.stylyts.presentation.contracts.shop.ShopListContract
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import java.util.*
import javax.inject.Inject

class ShopListPresenter @Inject constructor(
    private val errorHelper: ErrorHelper,
    private val searchProfileUseCase: SearchProfileUseCase
) : ShopListContract.Presenter {

    private lateinit var view: ShopListContract.View

    override fun disposeRequests() {
        searchProfileUseCase.clear()
    }

    override fun attach(view: ShopListContract.View) {
        this.view = view
    }

    override fun getShops(
        token: String,
        currentId: Int
    ) {
        searchProfileUseCase.initParams(
            token = token,
            username = EMPTY_STRING,
            isBrand = true
        )
        searchProfileUseCase.execute(object : DisposableSingleObserver<ResultsModel<UserModel>>() {
            override fun onSuccess(t: ResultsModel<UserModel>) {
                val preparedResults: MutableList<ShopListItem> = mutableListOf()
                val characterList: MutableList<String> = mutableListOf()

                sortedShopList(
                    results = t.results.filter { it.id != currentId }
                ).map { list ->
                    list.map {
                        preparedResults.add(it)
                    }

                    characterList.add(list[0].item as String)
                }

                view.processShops(shopList = preparedResults)
                view.processCharacter(character = characterList)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    override fun searchShop(
        token: String,
        username: String
    ) {
        searchProfileUseCase.initParams(
            token = token,
            username = username,
            isBrand = true
        )
        searchProfileUseCase.execute(object : DisposableSingleObserver<ResultsModel<UserModel>>() {
            override fun onSuccess(t: ResultsModel<UserModel>) {
                val preparedResults: MutableList<ShopListItem> = mutableListOf()

                t.results.map {
                    preparedResults.add(
                        ShopListItem(id = it.id, item = it)
                    )
                }

                view.processShops(shopList = preparedResults)
            }

            override fun onError(e: Throwable) {
                view.displayMessage(msg = errorHelper.processError(e))
            }
        })
    }

    private fun sortedShopList(results: List<UserModel>): List<List<ShopListItem>> {
        val list: MutableList<MutableList<ShopListItem>> = mutableListOf()

        results.map { shop ->
            var item: ShopListItem?
            var position: Int = -1
            var counter = 0

            list.map { filterList ->
                item = filterList.find {
                    it.item == shop.username.substring(0, 1)
                        .toUpperCase(Locale.getDefault())
                }

                if (item != null) {
                    position = counter
                }

                counter++
            }

            if (position != -1) {
                list[position].add(
                    ShopListItem(
                        id = shop.id,
                        item = shop
                    )
                )
            } else {
                val character = ShopListItem(
                    id = 0,
                    item = shop.username.substring(0, 1).toUpperCase(Locale.getDefault())
                )

                val newBrand = ShopListItem(
                    id = shop.id,
                    item = shop
                )

                list.add(mutableListOf(character, newBrand))
            }
        }

        list.sortBy { (it[0].item as String) }

        return list
    }
}