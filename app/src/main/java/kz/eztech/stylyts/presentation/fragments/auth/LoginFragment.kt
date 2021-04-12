package kz.eztech.stylyts.presentation.fragments.auth

import android.content.Intent
import android.view.View
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.presentation.activity.AuthorizationActivity
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.auth.LoginContract
import kz.eztech.stylyts.presentation.presenters.auth.LoginPresenter
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class LoginFragment : BaseFragment<AuthorizationActivity>(), LoginContract.View,
    View.OnClickListener {

    @Inject lateinit var presenter: LoginPresenter

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeViews() {}

    override fun initializeViewsData() {}

    override fun initializeListeners() {
        button_fragment_login_sign_up.setOnClickListener(this)
        text_view_fragment_login_forgot_password.setOnClickListener(this)
        button_fragment_login_sign_in.setOnClickListener(this)
    }

    override fun initializeArguments() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) = displayToast(msg)

    override fun isFragmentVisible(): Boolean = isVisible

    override fun getLayoutId(): Int = R.layout.fragment_login

    override fun getContractView(): BaseView = this

    override fun onClick(v: View?) {
        hideSoftWareKeyboard()

        when (v?.id) {
            R.id.button_fragment_login_sign_up -> {
                view?.findNavController()
                    ?.navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            R.id.text_view_fragment_login_forgot_password -> {
                view?.findNavController()
                    ?.navigate(R.id.action_loginFragment_to_refreshPasswordFragment)
            }
            R.id.button_fragment_login_sign_in -> checkData()
        }
    }

    override fun displayProgress() {
        include_base_progress.show()
    }

    override fun hideProgress() {
        include_base_progress.hide()
    }

    override fun processLoginUser(authModel: AuthModel) {
        authModel.let {
            currentActivity.saveSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY, it.token.access)
            currentActivity.saveSharedPrefByKey(SharedConstants.REFRESH_TOKEN_KEY, it.token.refresh)
            currentActivity.saveSharedPrefByKey(SharedConstants.USER_ID_KEY, it.user.id)

            startActivity(Intent(currentActivity, MainActivity::class.java))
            currentActivity.finish()
        }
    }

    override fun checkData() {
        val email = editText_fragment_login_username.text
        val password = editText_fragment_login_password.text

        if (email.isNotEmpty() && password.isNotEmpty()) {
            val data = HashMap<String, Any>()
            data["username"] = email.toString()
            data["password"] = password.toString()

            presenter.loginUser(data)
        } else {
            displayMessage(msg = getString(R.string.edit_profile_fill_data))
        }
    }
}