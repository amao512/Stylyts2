package kz.eztech.stylyts

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import kz.eztech.stylyts.di.ApplicationComponent
import kz.eztech.stylyts.di.DaggerApplicationComponent
import kz.eztech.stylyts.di.modules.*
import kz.eztech.stylyts.presentation.utils.helpers.LocaleHelper
import org.koin.core.context.startKoin

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

        startKoin {
            modules(viewModelModule)
        }
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
            .clothesModule(ClothesModule())
            .postsModule(PostsModule())
            .collectionModule(CollectionModule())
            .userModule(UserModule())
            .outfitsModule(OutfitsModule())
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