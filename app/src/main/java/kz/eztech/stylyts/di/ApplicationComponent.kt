package kz.eztech.stylyts.di

import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.di.modules.*
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.dialogs.cart.CartDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.ConstructorFilterDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.SaveClothesAcceptDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.TagChooserDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.UserSearchDialog
import kz.eztech.stylyts.presentation.dialogs.filter.FilterDialog
import kz.eztech.stylyts.presentation.dialogs.profile.EditProfileDialog
import kz.eztech.stylyts.presentation.dialogs.settings.ExitDialog
import kz.eztech.stylyts.presentation.fragments.address.AddressFragment
import kz.eztech.stylyts.presentation.fragments.auth.LoginFragment
import kz.eztech.stylyts.presentation.fragments.auth.RefreshPasswordFragment
import kz.eztech.stylyts.presentation.fragments.auth.RegistrationFragment
import kz.eztech.stylyts.presentation.fragments.clothes.ClothesDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionItemFragment
import kz.eztech.stylyts.presentation.fragments.collection.CollectionsFragment
import kz.eztech.stylyts.presentation.fragments.collection.CommentsFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CleanBackgroundFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CreateCollectionAcceptFragment
import kz.eztech.stylyts.presentation.fragments.main.MainFragment
import kz.eztech.stylyts.presentation.fragments.order_constructor.OrderingFragment
import kz.eztech.stylyts.presentation.fragments.order_constructor.PaymentFragment
import kz.eztech.stylyts.presentation.fragments.card.SaveCardFragment
import kz.eztech.stylyts.presentation.fragments.card.CardFragment
import kz.eztech.stylyts.presentation.fragments.order.UserOrderDetailFragment
import kz.eztech.stylyts.presentation.fragments.order.OrderListFragment
import kz.eztech.stylyts.presentation.fragments.order_constructor.SelectDeliveryWayFragment
import kz.eztech.stylyts.presentation.fragments.order_constructor.self_pickup.PickupPointsFragment
import kz.eztech.stylyts.presentation.fragments.profile.MyDataFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.search.SearchFragment
import kz.eztech.stylyts.presentation.fragments.search.SearchItemFragment
import kz.eztech.stylyts.presentation.fragments.shop.*
import kz.eztech.stylyts.presentation.fragments.users.UserSubsFragment
import kz.eztech.stylyts.presentation.fragments.users.UserSubsItemFragment
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
    MainModule::class,
    ProfileModule::class,
    AddressModule::class,
    SearchModule::class,
    ClothesModule::class,
    PostsModule::class,
    UserModule::class,
    OutfitsModule::class,
    WardrobeModule::class,
    CommentsModule::class,
    OrderModule::class
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
    fun inject(fragment: MainFragment)
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

    fun inject(dialog: TagChooserDialog)
    fun inject(dialog: CartDialog)
    fun inject(dialog: UserSearchDialog)
    fun inject(dialog: EditProfileDialog)
    fun inject(dialog: ExitDialog)
    fun inject(dialog: FilterDialog)
    fun inject(dialog: SaveClothesAcceptDialog)
}