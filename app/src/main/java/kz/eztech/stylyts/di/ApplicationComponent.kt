package kz.eztech.stylyts.di

import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.CreateCollectionAcceptDialog
import kz.eztech.stylyts.presentation.dialogs.profile.EditProfileDialog
import kz.eztech.stylyts.presentation.fragments.collection.CollectionItemFragment
import kz.eztech.stylyts.presentation.fragments.collection.ItemDetailFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CleanBackgroundFragment
import kz.eztech.stylyts.presentation.fragments.main.MainFragment
import kz.eztech.stylyts.presentation.fragments.profile.CardFragment
import kz.eztech.stylyts.presentation.fragments.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.shop.ShopItemFragment
import kz.eztech.stylyts.di.modules.*
import kz.eztech.stylyts.presentation.dialogs.CartDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.ConstructorFilterDialog
import kz.eztech.stylyts.presentation.dialogs.collection_constructor.UserSearchDialog
import kz.eztech.stylyts.presentation.dialogs.settings.ExitDialog
import kz.eztech.stylyts.presentation.fragments.address.AddressFragment
import kz.eztech.stylyts.presentation.fragments.auth.LoginFragment
import kz.eztech.stylyts.presentation.fragments.auth.RefreshPasswordFragment
import kz.eztech.stylyts.presentation.fragments.auth.RegistrationFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.collection_constructor.PhotoChooserFragment
import kz.eztech.stylyts.presentation.fragments.search.SearchItemFragment
import kz.eztech.stylyts.presentation.fragments.shop.CategoryTypeDetailFragment
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
    CollectionConstructorModule::class,
    CollectionModule::class
])
interface ApplicationComponent {

    val retrofit: Retrofit

    fun okHttpClient(): OkHttpClient

    fun inject(application: StylytsApp)
    fun inject(fragment: RegistrationFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: RefreshPasswordFragment)
    fun inject(fragment: ShopItemFragment)
    fun inject(fragment: CategoryTypeDetailFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: CollectionConstructorFragment)
    fun inject(fragment: MainFragment)
    fun inject(fragment: ItemDetailFragment)
    fun inject(fragment: PhotoChooserFragment)
    fun inject(fragment: CollectionItemFragment)
    fun inject(fragment: ConstructorFilterDialog)
    fun inject(fragment: CleanBackgroundFragment)
    fun inject(fragment: AddressFragment)
    fun inject(fragment: CardFragment)
    fun inject(fragment: SearchItemFragment)

    fun inject(dialog: CartDialog)
    fun inject(dialog: UserSearchDialog)
    fun inject(dialog: EditProfileDialog)
    fun inject(dialog: ExitDialog)
    fun inject(dialog: CreateCollectionAcceptDialog)
}