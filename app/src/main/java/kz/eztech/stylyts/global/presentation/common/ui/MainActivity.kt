package kz.eztech.stylyts.global.presentation.common.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.data.constants.SharedConstants
import kz.eztech.stylyts.global.domain.models.common.ErrorModel
import kz.eztech.stylyts.auth.domain.models.TokenModel
import kz.eztech.stylyts.global.presentation.base.BaseActivity
import kz.eztech.stylyts.global.presentation.common.contracts.MainActivityContract
import kz.eztech.stylyts.global.presentation.common.presenters.MainActivityPresenter
import kz.eztech.stylyts.utils.EMPTY_STRING
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class MainActivity : BaseActivity(), MainActivityContract.View {

    @Inject lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))

        initializeDependency()
        initializePresenter()
        checkToken()
    }

    override fun processVerification(errorModel: ErrorModel) {
        errorModel.detail?.let {
            if (it.isNotBlank()) {
                presenter.refreshToken(
                    refreshToken = getSharedPrefByKey(SharedConstants.REFRESH_TOKEN_KEY) ?: EMPTY_STRING
                )
            }
        }
    }

    override fun processRefresh(tokenModel: TokenModel) {
        saveSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY, tokenModel.access)
        saveSharedPrefByKey(SharedConstants.REFRESH_TOKEN_KEY, tokenModel.refresh)
    }

    fun hideBottomNavigationView() {
        bottomNavigationView.hide()
    }

    fun displayBottomNavigationView() {
        bottomNavigationView.show()
    }

    private fun initializeDependency() {
        (application as StylytsApp).applicationComponent.inject(activity = this)
    }

    private fun initializePresenter() {
        presenter.attach(view = this)
    }

    private fun checkToken() {
        presenter.verifyToken(token = getSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY) ?: EMPTY_STRING)
    }
}