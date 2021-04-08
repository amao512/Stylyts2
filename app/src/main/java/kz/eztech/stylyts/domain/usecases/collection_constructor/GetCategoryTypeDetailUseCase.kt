package kz.eztech.stylyts.domain.usecases.collection_constructor

import io.reactivex.Scheduler
import io.reactivex.Single
import kz.eztech.stylyts.data.api.RestConstants
import kz.eztech.stylyts.domain.models.ResultsModel
import kz.eztech.stylyts.domain.models.clothes.ClothesModel
import kz.eztech.stylyts.domain.repository.clothes.ClothesDomainRepository
import kz.eztech.stylyts.domain.usecases.BaseUseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ruslan Erdenoff on 18.12.2020.
 */
class GetCategoryTypeDetailUseCase @Inject constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private var clothesDomainRepository: ClothesDomainRepository
) : BaseUseCase<ResultsModel<ClothesModel>>(executorThread, uiThread) {

    private lateinit var token: String
    private lateinit var data: HashMap<String, Any>

    override fun createSingleObservable(): Single<ResultsModel<ClothesModel>> {
        return clothesDomainRepository.getCategoryTypeDetail(
            token = token,
            data = data
        )
    }

    fun initParams(
        token: String,
        data: HashMap<String, Any>
    ) {
        this.token = RestConstants.HEADERS_AUTH_FORMAT.format(token)
        this.data = data
    }
}