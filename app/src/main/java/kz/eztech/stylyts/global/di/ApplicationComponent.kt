package kz.eztech.stylyts.global.di

import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.auth.di.AuthModule
import kz.eztech.stylyts.global.di.modules.*
import kz.eztech.stylyts.home.di.HomeModule
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.collection_constructor.presentation.ui.dialogs.ConstructorFilterDialog
import kz.eztech.stylyts.collection_constructor.presentation.ui.dialogs.SaveClothesAcceptDialog
import kz.eztech.stylyts.collection_constructor.presentation.ui.dialogs.TagChooserDialog
import kz.eztech.stylyts.global.presentation.userSearch.ui.UserSearchDialog
import kz.eztech.stylyts.global.presentation.filter.ui.FilterDialog
import kz.eztech.stylyts.profile.presentation.profile.ui.dialogs.EditProfileDialog
import kz.eztech.stylyts.profile.presentation.settings.ui.dialogs.ExitDialog
import kz.eztech.stylyts.profile.presentation.address.ui.AddressFragment
import kz.eztech.stylyts.auth.presentation.ui.LoginFragment
import kz.eztech.stylyts.auth.presentation.ui.RefreshPasswordFragment
import kz.eztech.stylyts.auth.presentation.ui.RegistrationFragment
import kz.eztech.stylyts.global.presentation.card.ui.CardFragment
import kz.eztech.stylyts.global.presentation.card.ui.SaveCardFragment
import kz.eztech.stylyts.global.presentation.clothes.ui.ClothesDetailFragment
import kz.eztech.stylyts.global.presentation.collection.ui.CollectionDetailFragment
import kz.eztech.stylyts.collections.presentation.ui.CollectionItemFragment
import kz.eztech.stylyts.collections.presentation.ui.CollectionsFragment
import kz.eztech.stylyts.global.presentation.collection.ui.CommentsFragment
import kz.eztech.stylyts.collection_constructor.presentation.ui.fragments.CleanBackgroundFragment
import kz.eztech.stylyts.collection_constructor.presentation.ui.fragments.CollectionConstructorFragment
import kz.eztech.stylyts.collection_constructor.presentation.ui.fragments.CreateCollectionAcceptFragment
import kz.eztech.stylyts.ordering.presentation.incomes.ui.IncomeDetailFragment
import kz.eztech.stylyts.home.presentation.home.ui.HomeFragment
import kz.eztech.stylyts.ordering.di.OrderModule
import kz.eztech.stylyts.ordering.di.ReferralsModule
import kz.eztech.stylyts.ordering.presentation.order.ui.OrderListFragment
import kz.eztech.stylyts.ordering.presentation.order.ui.ShopOrderDetailFragment
import kz.eztech.stylyts.ordering.presentation.order.ui.UserOrderDetailFragment
import kz.eztech.stylyts.ordering.presentation.cart.ui.CartFragment
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.fragments.OrderingFragment
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.fragments.PaymentFragment
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.fragments.SelectDeliveryWayFragment
import kz.eztech.stylyts.ordering.presentation.order_constructor.ui.fragments.self_pickup.PickupPointsFragment
import kz.eztech.stylyts.profile.presentation.profile.ui.MyDataFragment
import kz.eztech.stylyts.profile.presentation.profile.ui.ProfileFragment
import kz.eztech.stylyts.ordering.presentation.incomes.ui.IncomesFragment
import kz.eztech.stylyts.search.presentation.search.ui.SearchFragment
import kz.eztech.stylyts.search.presentation.search.ui.SearchItemFragment
import kz.eztech.stylyts.profile.di.ProfileModule
import kz.eztech.stylyts.profile.presentation.subscriptions.ui.UserSubsFragment
import kz.eztech.stylyts.profile.presentation.subscriptions.ui.UserSubsItemFragment
import kz.eztech.stylyts.profile.presentation.profile.ui.ShopProfileFragment
import kz.eztech.stylyts.search.di.SearchModule
import kz.eztech.stylyts.search.presentation.shop.ui.ShopCategoryListFragment
import kz.eztech.stylyts.search.presentation.shop.ui.ShopClothesListFragment
import kz.eztech.stylyts.search.presentation.shop.ui.ShopItemFragment
import kz.eztech.stylyts.search.presentation.shop.ui.ShopListFragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
@Singleton
@dagger.Component(modules = [
    ApplicationModule::class,
    NetworkModule::class,
    AuthModule::class,
    HomeModule::class,
    ProfileModule::class,
    AddressModule::class,
    SearchModule::class,
    ClothesModule::class,
    PostsModule::class,
    UserModule::class,
    OutfitsModule::class,
    WardrobeModule::class,
    CommentsModule::class,
    OrderModule::class,
    ReferralsModule::class
])
interface ApplicationComponent {

    val retrofit: Retrofit

    fun okHttpClient(): OkHttpClient

    fun inject(application: StylytsApp)
    fun inject(activity: MainActivity)

    fun inject(fragment: RegistrationFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: RefreshPasswordFragment)
    fun inject(fragment: ShopItemFragment)
    fun inject(fragment: ShopClothesListFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: MyDataFragment)
    fun inject(fragment: CollectionConstructorFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: ClothesDetailFragment)
    fun inject(fragment: CollectionItemFragment)
    fun inject(fragment: ConstructorFilterDialog)
    fun inject(fragment: CleanBackgroundFragment)
    fun inject(fragment: AddressFragment)
    fun inject(fragment: CardFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SearchItemFragment)
    fun inject(fragment: ShopCategoryListFragment)
    fun inject(fragment: UserSubsFragment)
    fun inject(fragment: UserSubsItemFragment)
    fun inject(fragment: CollectionsFragment)
    fun inject(fragment: CollectionDetailFragment)
    fun inject(fragment: CreateCollectionAcceptFragment)
    fun inject(fragment: CommentsFragment)
    fun inject(fragment: ShopProfileFragment)
    fun inject(fragment: ShopListFragment)
    fun inject(fragment: OrderingFragment)
    fun inject(fragment: SaveCardFragment)
    fun inject(fragment: PaymentFragment)
    fun inject(fragment: OrderListFragment)
    fun inject(fragment: PickupPointsFragment)
    fun inject(fragmentUser: UserOrderDetailFragment)
    fun inject(fragment: SelectDeliveryWayFragment)
    fun inject(fragment: ShopOrderDetailFragment)
    fun inject(fragment: CartFragment)
    fun inject(fragment: IncomesFragment)
    fun inject(fragment: IncomeDetailFragment)

    fun inject(dialog: TagChooserDialog)
    fun inject(dialog: UserSearchDialog)
    fun inject(dialog: EditProfileDialog)
    fun inject(dialog: ExitDialog)
    fun inject(dialog: FilterDialog)
    fun inject(dialog: SaveClothesAcceptDialog)
}