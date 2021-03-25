package kz.eztech.stylyts

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import kz.eztech.stylyts.di.modules.AddressModule
import kz.eztech.stylyts.di.modules.AuthModule
import kz.eztech.stylyts.di.modules.CollectionModule
import kz.eztech.stylyts.di.modules.CollectionConstructorModule
import kz.eztech.stylyts.di.ApplicationComponent
import kz.eztech.stylyts.di.DaggerApplicationComponent
import kz.eztech.stylyts.di.modules.ApplicationModule
import kz.eztech.stylyts.di.modules.NetworkModule
import kz.eztech.stylyts.presentation.utils.helpers.LocaleHelper
import kz.eztech.stylyts.di.modules.MainModule
import kz.eztech.stylyts.di.modules.ProfileModule
import kz.eztech.stylyts.di.modules.SearchModule

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
class StylytsApp : Application(){

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        initApplicationComponent()
        initLocality()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun initApplicationComponent() {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(mApplication = this))
            .networkModule(NetworkModule())
            .authModule(AuthModule())
            .mainModule(MainModule())
            .profileModule(ProfileModule())
            .addressModule(AddressModule())
            .searchModule(SearchModule(mApplication = this))
            .collectionConstructorModule(CollectionConstructorModule())
            .collectionModule(CollectionModule())
            .build()

        applicationComponent.inject(application = this)
    }

    private fun initLocality() {
        LocaleHelper.setLocaleFromSharedPref(context = this)
    }

    companion object {
        var instance: StylytsApp? = null
            private set

        val isNetworkAvailable: Boolean
            get() {
               return true
            }
    }
}