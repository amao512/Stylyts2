package kz.eztech.stylyts

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.multidex.MultiDex
import kz.eztech.stylyts.di.component.ApplicationComponent
import kz.eztech.stylyts.di.component.DaggerApplicationComponent
import kz.eztech.stylyts.di.modules.ApplicationModule
import kz.eztech.stylyts.di.modules.NetworkModule

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
class StylytsApp : Application(){
    lateinit var applicationComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        instance = this

        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .networkModule(NetworkModule())
            .build()
        applicationComponent.inject(this)

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
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