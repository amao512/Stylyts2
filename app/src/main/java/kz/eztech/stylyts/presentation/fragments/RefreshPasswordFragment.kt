package kz.eztech.stylyts.presentation.fragments

import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_refresh_password.*
import kotlinx.android.synthetic.main.fragment_refresh_password.include_toolbar
import kotlinx.android.synthetic.main.fragment_refresh_password.include_base_progress
import kotlinx.android.synthetic.main.fragment_refresh_password_complete_start.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.presentation.activity.AuthorizationActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.RefreshPasswordContract
import kz.eztech.stylyts.presentation.presenters.RefreshPasswordPresenter
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import javax.inject.Inject

class RefreshPasswordFragment : BaseFragment<AuthorizationActivity>(), RefreshPasswordContract.View,
    View.OnClickListener {

    @Inject lateinit var presenter: RefreshPasswordPresenter

    enum class RefreshStates { SEND_PASSWORD, REFRESH_PASSWORD, COMPLETE }

    private var refreshState = RefreshStates.SEND_PASSWORD

    override fun getLayoutId(): Int = R.layout.fragment_refresh_password

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar) {
            toolbar_left_corner_action_image_button.show()
            toolbar_back_text_view.show()
            toolbar_title_text_view.hide()
            toolbar_right_corner_action_image_button.hide()
            customizeActionToolBar(this)
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {}

    override fun initializeViews() {}

    override fun initializeListeners() {
        button_fragment_refresh_password_send_password.setOnClickListener(this)
        button_fragment_refresh_password_refresh_password.setOnClickListener(this)
        button_fragment_refresh_password_refresh_password_compete.setOnClickListener(this)
    }

    override fun processPostInitialization() {
        processState()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun changeState(state: RefreshStates) {
        refreshState = state
        processState()
    }

    override fun processState() {
        when (refreshState) {
            RefreshStates.SEND_PASSWORD -> {
                constraint_layout_fragment_refresh_password_forgot_container.show()
                constraint_layout_fragment_refresh_password_refresh_container.hide()
                constraint_layout_fragment_refresh_password_complete_container.hide()
            }
            RefreshStates.REFRESH_PASSWORD -> {
                constraint_layout_fragment_refresh_password_forgot_container.hide()
                constraint_layout_fragment_refresh_password_refresh_container.show()
                constraint_layout_fragment_refresh_password_complete_container.hide()
            }
            RefreshStates.COMPLETE -> {
                constraint_layout_fragment_refresh_password_forgot_container.hide()
                constraint_layout_fragment_refresh_password_refresh_container.hide()
                constraint_layout_fragment_refresh_password_complete_container.show()

                val fade = AnimationUtils.loadAnimation(currentActivity, android.R.anim.slide_in_left);
                fade.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        Looper.myLooper()?.let {
                            android.os.Handler(it).postDelayed({
                                constraint_layout_fragment_refresh_password_complete_container.transitionToEnd()
                            }, 500)
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                })
                constraint_layout_fragment_refresh_password_complete_container.startAnimation(fade)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_fragment_refresh_password_send_password -> checkGenerateForgotPasswordData()
            R.id.button_fragment_refresh_password_refresh_password -> checkSetNewPasswordData()
            R.id.button_fragment_refresh_password_refresh_password_compete -> currentActivity.onBackPressed()
        }
    }

    override fun checkGenerateForgotPasswordData() {
        if (editText_fragment_refresh_password_email.text.isNotEmpty()) {
            presenter.generateForgotPassword(editText_fragment_refresh_password_email.text.toString())
        } else {
            displayMessage("Заполните поля")
        }
    }

    override fun checkSetNewPasswordData() {
        if (edit_text_fragment_refresh_password_refresh_password.text.isNotEmpty()) {
            presenter.setNewPassword(edit_text_fragment_refresh_password_refresh_password.text.toString())
        } else {
            displayMessage("Заполните поля")
        }
    }

    override fun displayProgress() {
        include_base_progress.show()
    }

    override fun hideProgress() {
        include_base_progress.hide()
    }
}