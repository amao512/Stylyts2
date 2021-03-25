package kz.eztech.stylyts.di.modules

import dagger.Module
import dagger.Provides
import kz.eztech.stylyts.data.repository.main.MainLentaRepository
import kz.eztech.stylyts.domain.repository.main.MainLentaDomainRepository

@Module
class MainModule {

    @Provides
    fun providesMainLentaRepository(mainLentaRepository: MainLentaRepository): MainLentaDomainRepository {
        return mainLentaRepository
    }
}