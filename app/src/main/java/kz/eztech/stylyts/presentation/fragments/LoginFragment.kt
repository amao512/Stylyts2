package kz.eztech.stylyts.presentation.fragments

import android.content.Intent
import android.view.View
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.AuthModel
import kz.eztech.stylyts.presentation.activity.AuthorizationActivity
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.LoginContract
import kz.eztech.stylyts.presentation.presenters.LoginPresenter
import javax.inject.Inject

class LoginFragment : BaseFragment<AuthorizationActivity>(), LoginContract.View,
    View.OnClickListener {

    @Inject
    lateinit var presenter: LoginPresenter

    override fun customizeActionBar() {}

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
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

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_fragment_login_sign_up -> {
                hideSoftWareKeyboard()
                view?.findNavController()
                    ?.navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            R.id.text_view_fragment_login_forgot_password -> {
                hideSoftWareKeyboard()
                view?.findNavController()
                    ?.navigate(R.id.action_loginFragment_to_refreshPasswordFragment)
            }
            R.id.button_fragment_login_sign_in -> {
                hideSoftWareKeyboard()
                checkData()
            }
        }
    }

    override fun displayProgress() {
        include_base_progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        include_base_progress.visibility = View.GONE
    }

    override fun processLoginUser(authModel: AuthModel) {
        currentActivity.saveSharedPrefByKey(SharedConstants.TOKEN_KEY, authModel.token)
        currentActivity.saveSharedPrefByKey(SharedConstants.USER_ID_KEY, authModel.user?.pk)
        currentActivity.saveSharedPrefByKey(SharedConstants.USERNAME_KEY, authModel.user?.username)
        startActivity(Intent(currentActivity, MainActivity::class.java))
        currentActivity.finish()
    }

    override fun checkData() {
        if (editText_fragment_login_email.text.isNotEmpty() &&
            editText_fragment_login_password.text.isNotEmpty()
        ) {
            val data = HashMap<String, Any>()
            data["username"] = editText_fragment_login_email.text.toString()
            data["password"] = editText_fragment_login_password.text.toString()
            presenter.loginUser(data)
        } else {
            displayMessage("Заполните данные")
        }
    }
}