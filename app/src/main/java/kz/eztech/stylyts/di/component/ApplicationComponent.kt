package kz.eztech.stylyts.di.component

import androidx.fragment.app.Fragment
import dagger.android.AndroidInjectionModule
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.di.modules.ApplicationModule
import kz.eztech.stylyts.di.modules.NetworkModule
import kz.eztech.stylyts.presentation.base.BaseActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.fragments.LoginFragment
import kz.eztech.stylyts.presentation.fragments.RefreshPasswordFragment
import kz.eztech.stylyts.presentation.fragments.RegistrationFragment
import kz.eztech.stylyts.presentation.fragments.main.MainFragment
import kz.eztech.stylyts.presentation.fragments.main.constructor.CollectionConstructorFragment
import kz.eztech.stylyts.presentation.fragments.main.profile.ProfileFragment
import kz.eztech.stylyts.presentation.fragments.main.shop.CategoryTypeDetailFragment
import kz.eztech.stylyts.presentation.fragments.main.shop.ShopItemFragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
@Singleton
@dagger.Component(modules = [ApplicationModule::class, NetworkModule::class])
interface ApplicationComponent {
    val retrofit: Retrofit
    fun okHttpClient() : OkHttpClient

    fun inject(application: StylytsApp)
    fun inject(fragment: RegistrationFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: RefreshPasswordFragment)
    fun inject(fragment: ShopItemFragment)
    fun inject(fragment: CategoryTypeDetailFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: CollectionConstructorFragment)
    fun inject(fragment: MainFragment)

}