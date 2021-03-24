package kz.eztech.stylyts.common.di

import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.auth.di.AuthModule
import kz.eztech.stylyts.auth.presentation.fragments.LoginFragment
import kz.eztech.stylyts.auth.presentation.fragments.RefreshPasswordFragment
import kz.eztech.stylyts.auth.presentation.fragments.RegistrationFragment
import kz.eztech.stylyts.common.di.modules.ApplicationModule
import kz.eztech.stylyts.common.di.modules.NetworkModule
import kz.eztech.stylyts.common.presentation.dialogs.CartDialog
import kz.eztech.stylyts.create_outfit.presentation.dialogs.ConstructorFilterDialog
import kz.eztech.stylyts.create_outfit.presentation.dialogs.UserSearchDialog
import kz.eztech.stylyts.common.presentation.fragments.MainFragment
import kz.eztech.stylyts.common.presentation.fragments.collections.CollectionItemFragment
import kz.eztech.stylyts.create_outfit.presentation.fragments.CleanBackgroundFragment
import kz.eztech.stylyts.create_outfit.presentation.fragments.CollectionConstructorFragment
import kz.eztech.stylyts.create_outfit.presentation.fragments.PhotoChooserFragment
import kz.eztech.stylyts.common.presentation.fragments.detail.ItemDetailFragment
import kz.eztech.stylyts.common.presentation.fragments.shop.CategoryTypeDetailFragment
import kz.eztech.stylyts.common.presentation.fragments.shop.ShopItemFragment
import kz.eztech.stylyts.profile.di.ProfileModule
import kz.eztech.stylyts.profile.presentation.dialogs.EditProfileDialog
import kz.eztech.stylyts.profile.presentation.fragments.AddressProfileFragment
import kz.eztech.stylyts.profile.presentation.fragments.CardFragment
import kz.eztech.stylyts.profile.presentation.fragments.ProfileFragment
import kz.eztech.stylyts.search.di.SearchModule
import kz.eztech.stylyts.search.presentation.fragments.SearchItemFragment
import kz.eztech.stylyts.settings.presentation.dialogs.ExitDialog
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
    ProfileModule::class,
    SearchModule::class
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
    fun inject(fragment: AddressProfileFragment)
    fun inject(fragment: CardFragment)
    fun inject(fragment: SearchItemFragment)

    fun inject(dialog: CartDialog)
    fun inject(dialog: UserSearchDialog)
    fun inject(dialog: EditProfileDialog)
    fun inject(dialog: ExitDialog)
}