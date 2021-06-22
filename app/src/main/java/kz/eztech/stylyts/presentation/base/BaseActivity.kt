package kz.eztech.stylyts.presentation.base

import android.content.Context
import android.content.res.Configuration
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import java.util.*

/**
 * Created by Ruslan Erdenoff on 19.11.2020.
 */
abstract class BaseActivity : AppCompatActivity() {

    var mToolbar: Toolbar? = null

    companion object {
        var dLocale: Locale? = null
    }

    init {
        updateConfig(this)
    }

    fun displayToast(msg: String) {
        //Snackbar.make()
        //Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    fun getTokenFromSharedPref(): String {
        return getSharedPrefByKey<String>(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING
    }

    fun getUserIdFromSharedPref(): Int {
        return getSharedPrefByKey<Int>(SharedConstants.USER_ID_KEY) ?: 0
    }

    fun getIsBrandFromSharedPref(): Boolean {
        return getSharedPrefByKey<Boolean>(SharedConstants.IS_BRAND_KEY) ?: false
    }

    inline fun <reified T> getSharedPrefByKey(key: String): T? {
        return try {
            val sharedPref =
                getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    ?: null
            sharedPref?.let {
                when (T::class) {
                    String::class -> {
                        return sharedPref.getString(key, "") as T
                    }
                    Int::class -> {
                        return sharedPref.getInt(key, 0) as T
                    }
                    Float::class -> {
                        return sharedPref.getLong(key, 0L) as T
                    }
                    Long::class -> {
                        return sharedPref.getLong(key, 0L) as T
                    }
                    Boolean::class -> {
                        return sharedPref.getBoolean(key, false) as T
                    }
                    else -> {
                        return null
                    }
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun saveSharedPrefByKey(key: String, value: Any?) {
        value?.let {
            val sharedPref =
                getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                    ?: return
            with(sharedPref.edit()) {
                when (it) {
                    is String -> putString(key, it)
                    is Int -> putInt(key, it)
                    is Float -> putFloat(key, it)
                    is Long -> putLong(key, it)
                    is Boolean -> putBoolean(key, it)
                }
                apply()
            }
        }
    }

    fun removeFromSharedPrefByKey(key: String) {
        val sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
                ?: return

        with(sharedPreferences.edit()) {
            remove(key)
            apply()
        }
    }

    private fun updateConfig(wrapper: ContextThemeWrapper) {
        if (dLocale == Locale(EMPTY_STRING)) return

        dLocale?.let {
            Locale.setDefault(it)
            val configuration = Configuration()
            configuration.setLocale(it)
            wrapper.applyOverrideConfiguration(configuration)
        }
    }
}