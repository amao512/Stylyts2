package kz.eztech.stylyts.presentation.fragments.auth

import android.app.DatePickerDialog
import android.view.View
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants.TOKEN_KEY
import kz.eztech.stylyts.data.models.SharedConstants.USER_ID_KEY
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.presentation.activity.AuthorizationActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.auth.RegistrationContract
import kz.eztech.stylyts.presentation.presenters.auth.RegistrationPresenter
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class RegistrationFragment : BaseFragment<AuthorizationActivity>(), RegistrationContract.View,
    View.OnClickListener {

    @Inject lateinit var presenter: RegistrationPresenter

    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDayOfMonth: Int? = null

    override fun customizeActionBar() {
        with(include_toolbar) {
            toolbar_left_corner_action_image_button.show()
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.hide()

            customizeActionToolBar(this, getString(R.string.fragment_registration_appbar_title))
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(this)
    }

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        text_view_fragment_registration_term.text = HtmlCompat.fromHtml(
            getString(R.string.fragment_registration_term),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    override fun initializeViews() {}

    override fun initializeListeners() {
        button_fragment_registration_date.setOnClickListener(this)
        button_fragment_registration_submit.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {
        displayToast(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun getLayoutId(): Int = R.layout.fragment_registration

    override fun getContractView(): BaseView = this

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_view_fragment_registration_term -> { }
            R.id.button_fragment_registration_date -> {
                var c: Calendar? = null

                if (mYear == null || mMonth == null || mDayOfMonth == null) {
                    c = Calendar.getInstance()
                    c?.let { cal ->
                        mYear = cal.get(Calendar.YEAR)
                        mMonth = cal.get(Calendar.MONTH)
                        mDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
                    }
                }

                val dpd = DatePickerDialog(currentActivity, { view, year, monthOfYear, dayOfMonth ->
                    mYear = year
                    mMonth = monthOfYear
                    mDayOfMonth = dayOfMonth
                    button_fragment_registration_date.setText("$mDayOfMonth / ${DateFormatSymbols().getMonths()[mMonth!! - 1]} / $mYear")

                }, mYear!!, mMonth!!, mDayOfMonth!!)

                dpd.show()
            }

            R.id.button_fragment_registration_submit -> {
                checkData()
            }
        }
    }

    override fun checkData() {
        if (edit_text_view_fragment_registration_name.text.isNotEmpty() &&
            edit_text_view_fragment_registration_last_name.text.isNotEmpty() &&
            edit_text_view_fragment_registration_email.text.isNotEmpty() &&
            edit_text_view_fragment_registration_password.text.isNotEmpty() &&
            edit_text_view_fragment_registration_password_repeat.text.isNotEmpty() &&
            mYear != null && mMonth != null && mDayOfMonth != null
        ) {
            if (edit_text_view_fragment_registration_password.text.toString() !=
                edit_text_view_fragment_registration_password_repeat.text.toString()
            ) {
                displayMessage("Пароли несовпадают")
            } else {
                processCheckedData()
            }
        } else {
            displayMessage("Заполните пустые поля")
        }
    }

    override fun processCheckedData() {
        val data = HashMap<String, Any>()
        data["email"] = edit_text_view_fragment_registration_email.text.toString()
        data["password"] = edit_text_view_fragment_registration_password.text.toString()
        data["name"] = edit_text_view_fragment_registration_name.text.toString()
        data["last_name"] = edit_text_view_fragment_registration_last_name.text.toString()
        data["date_of_birth"] = "$mYear-$mMonth-$mDayOfMonth"
        data["should_send_mail"] = true
        data["username"] = edit_text_view_fragment_registration_username.text.toString()

        presenter.registerUser(data)
    }

    override fun processSuccessRegistration() {
        currentActivity.onBackPressed()
        displayMessage("Успешно")
    }

    override fun displayProgress() {
        include_base_progress.show()
    }

    override fun hideProgress() {
        include_base_progress.hide()
    }

    override fun processUser(authModel: AuthModel) {
        currentActivity.saveSharedPrefByKey(TOKEN_KEY, authModel.token)
        currentActivity.saveSharedPrefByKey(USER_ID_KEY, authModel.user?.id)
        processSuccessRegistration()
    }
}