package kz.eztech.stylyts.search.presentation.shop.data

import android.content.Context
import kz.eztech.stylyts.R
import kz.eztech.stylyts.global.domain.models.filter.CollectionFilterModel
import kz.eztech.stylyts.global.domain.models.user.UserModel
import kz.eztech.stylyts.search.presentation.shop.data.models.ShopListItem
import java.util.*

interface UIShopListData {

    fun getFilterList(context: Context): List<CollectionFilterModel>

    fun getShopList(
        usersList: List<UserModel>,
        currentUserId: Int
    ): List<ShopListItem>

    fun getCharacterList(list: List<UserModel>): List<String>
}

class UIShopListDataDelegate : UIShopListData {

    override fun getFilterList(context: Context): List<CollectionFilterModel> {
        val filterList: MutableList<CollectionFilterModel> = mutableListOf()

//        filterList.add(
//            CollectionFilterModel(
//                id = 1,
//                name = getString(R.string.filter_list_filter),
//                icon = R.drawable.ic_filter
//            )
//        )

        filterList.add(
            CollectionFilterModel(
                id = 1,
                name = context.getString(R.string.filter_favorite_brands),
                icon = R.drawable.ic_baseline_favorite_border_24
            )
        )

        return filterList
    }

    override fun getShopList(
        usersList: List<UserModel>,
        currentUserId: Int
    ): List<ShopListItem> {
        val preparedResults: MutableList<ShopListItem> = mutableListOf()

        getSortedShopList(
            results = usersList.filter { it.id != currentUserId }
        ).map { list ->
            list.map {
                preparedResults.add(it)
            }
        }

        return preparedResults
    }

    override fun getCharacterList(list: List<UserModel>): List<String> {
        val characterList: MutableList<String> = mutableListOf()

        getSortedShopList(list).map {
            characterList.add(it[0].item as String)
        }

        return characterList
    }

    private fun getSortedShopList(results: List<UserModel>): List<List<ShopListItem>> {
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