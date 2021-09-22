package kz.eztech.stylyts.global.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.global.data.api.ClothesApi
import kz.eztech.stylyts.global.data.repositories.ClothesRepository
import kz.eztech.stylyts.global.domain.repositories.ClothesDomainRepository
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Asylzhan Seytbek on 06.04.2021.
 */
@Module
class ClothesModule {

    @Provides
    @Singleton
    fun provideClothesApi(retrofit: Retrofit): ClothesApi {
        return retrofit.create(ClothesApi::class.java)
    }

    @Provides
    fun provideClothesRepository(clothesRepository: ClothesRepository): ClothesDomainRepository {
        return clothesRepository
    }
}