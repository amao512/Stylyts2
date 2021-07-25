package kz.eztech.stylyts.di.modules

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.BuildConfig
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.data.helpers.MyTLSSocketFactory
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Created by Ruslan Erdenoff on 18.11.2020.
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun providesOkHttpCache(application: Application): Cache {
        val cacheSize = 50 * 1024 * 1024

        return Cache(application!!.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    internal fun providesGson(): Gson = GsonBuilder()
        .serializeNulls()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
        .setDateFormat("yyyy-MM-dd")
        .create()

    @Provides
    @Singleton
    internal fun providesRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(RestConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        return if (BuildConfig.DEBUG) HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BODY
        ) else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
    }

    @Provides
    @Singleton
    internal fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        cache: Cache,
        sharedPreferences: SharedPreferences
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", getToken(sharedPreferences))
                    .build()

                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .cache(cache)

        return enableTls12OnPreLollipop(client).build()
    }

    fun getToken(sharedPreferences: SharedPreferences): String {
        return RestConstants.HEADERS_AUTH_FORMAT.format(
            sharedPreferences.getString(
                SharedConstants.ACCESS_TOKEN_KEY,
                EMPTY_STRING
            ).orEmpty()
        )
    }

    private fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                val sc = SSLContext.getInstance("TLSv1.3")
                sc.init(null, null, null)

                val javaDefaultTrustManager =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                javaDefaultTrustManager.init(null as KeyStore?)
                val defTrustManager = javaDefaultTrustManager.trustManagers[0] as X509TrustManager

                client.sslSocketFactory(MyTLSSocketFactory(sc.socketFactory), defTrustManager)
                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_3)
                    .build()
                val specs: MutableList<ConnectionSpec> = ArrayList()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)
                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }
        }

        return client
    }


}