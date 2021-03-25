package kz.eztech.stylyts.common.presentation.utils.helpers

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager
import kz.eztech.stylyts.common.presentation.base.BaseActivity
import kz.eztech.stylyts.common.presentation.utils.EMPTY_STRING
import java.util.*

/**
 * Created by Asylzhan Seytbek on 25.03.2021.
 */
object LocaleHelper {

    private const val SELECTED_LANGUAGE = "locale_selected_language"

    fun setLocale(
        context: Context,
        language: String
    ): Context? {
        persist(context, language)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language)
        }

        return updateResourcesLegacy(context, language)
    }

    fun setLocaleFromSharedPref(context: Context) {
        val language = PreferenceManager.getDefaultSharedPreferences(context).getString(SELECTED_LANGUAGE, EMPTY_STRING)

        Log.d("TAG", "Styles App")
        BaseActivity.dLocale = Locale(language)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun persist(
        context: Context,
        language: String
    ) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putString(SELECTED_LANGUAGE, language)
            apply()
        }

        Log.d("TAG", "locale helper")
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        BaseActivity.dLocale = locale

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)
        BaseActivity.dLocale = locale

        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration

        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }
}