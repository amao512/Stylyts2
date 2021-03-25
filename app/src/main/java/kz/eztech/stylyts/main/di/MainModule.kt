package kz.eztech.stylyts.main.di

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.main.data.repository.MainLentaRepository
import kz.eztech.stylyts.main.domain.repository.MainLentaDomainRepository

@Module
class MainModule {

    @Provides
    fun providesMainLentaRepository(mainLentaRepository: MainLentaRepository): MainLentaDomainRepository {
        return mainLentaRepository
    }
}