package kz.eztech.stylyts.presentation.fragments.auth

import android.content.Intent
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.domain.models.auth.AuthModel
import kz.eztech.stylyts.domain.models.auth.ExistsUsernameModel
import kz.eztech.stylyts.presentation.activity.AuthorizationActivity
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.auth.RegistrationContract
import kz.eztech.stylyts.presentation.presenters.auth.RegistrationPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class RegistrationFragment : BaseFragment<AuthorizationActivity>(), RegistrationContract.View,
    View.OnClickListener {

    @Inject lateinit var presenter: RegistrationPresenter

    private var isUsernameChecked = false
    private var mYear: Int? = null
    private var mMonth: Int? = null
    private var mDayOfMonth: Int? = null

    companion object {
        private const val DATE_TEXT_FORMAT = "%s / %s / %s"
    }

    override fun customizeActionBar() {
        with(include_toolbar) {
            toolbar_left_corner_action_image_button.setBackgroundResource(R.drawable.ic_baseline_close_24)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.hide()
            toolbar_bottom_border_view.hide()

            customizeActionToolBar(toolbar = this, title = getString(R.string.fragment_registration_appbar_title))
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
        toolbar_left_corner_action_image_button.setOnClickListener(this)
        fragment_registration_check_username_button.setOnClickListener(this)
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
            R.id.toolbar_left_corner_action_image_button -> onCloseButtonClick()
            R.id.fragment_registration_check_username_button -> onCheckUsernameButtonClick()
            R.id.text_view_fragment_registration_term -> {}
            R.id.button_fragment_registration_date -> setDate()
            R.id.button_fragment_registration_submit -> checkData()
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
                displayMessage(msg = getString(R.string.registration_passwords_not_match))
            } else {
                processCheckedData()
            }
        } else {
            displayMessage(msg = getString(R.string.registration_fill_empty_fields))
        }
    }

    override fun processCheckedData() {
        val data = HashMap<String, Any>()
        data["email"] = edit_text_view_fragment_registration_email.text.toString()
        data["password"] = edit_text_view_fragment_registration_password.text.toString()
        data["first_name"] = edit_text_view_fragment_registration_name.text.toString()
        data["last_name"] = edit_text_view_fragment_registration_last_name.text.toString()
        data["date_of_birth"] = "$mYear-$mMonth-$mDayOfMonth"
        data["is_brand"] = false
        data["username"] = edit_text_view_fragment_registration_username.text.toString()
            .toLowerCase(Locale.getDefault())
        data["gender"] = "male"
        data["should_send_mail"] = !fragment_registration_should_send_mail_check_box.isChecked

        presenter.registerUser(data)
    }

    override fun processSuccessRegistration() {
        displayMessage(msg = getString(R.string.registration_success))
        startActivity(Intent(currentActivity, MainActivity::class.java))
        currentActivity.finish()
    }

    override fun displayProgress() {
        include_base_progress.show()
    }

    override fun hideProgress() {
        include_base_progress.hide()
    }

    override fun processUser(authModel: AuthModel) {
        authModel.let {
            currentActivity.saveSharedPrefByKey(SharedConstants.ACCESS_TOKEN_KEY, it.token?.access)
            currentActivity.saveSharedPrefByKey(SharedConstants.REFRESH_TOKEN_KEY, it.token?.refresh)

            processSuccessRegistration()
        }
    }

    override fun isUsernameExists(existsUsernameModel: ExistsUsernameModel) {
        if (existsUsernameModel.exists) {
            displayMessage(msg = getString(R.string.registration_username_is_already_exists))
        } else {
            toolbar_title_text_view.show()
            toolbar_back_text_view.show()
            toolbar_left_corner_action_image_button.hide()
            fragment_registration_username_linear_layout.hide()
            fragment_registration_fields_scroll_view.show()

            isUsernameChecked = true
        }
    }

    private fun onCloseButtonClick() {
        when (isUsernameChecked) {
            true -> {
                fragment_registration_fields_scroll_view.hide()
                fragment_registration_username_linear_layout.show()
            }
            false -> findNavController().navigateUp()
        }
    }

    private fun onCheckUsernameButtonClick() {
        if (edit_text_view_fragment_registration_username.text.isNotBlank()) {
            presenter.checkUsername(
                username = edit_text_view_fragment_registration_username.text.toString()
                    .toLowerCase(Locale.getDefault())
            )
        } else {
            displayMessage(msg = getString(R.string.registration_fill_field))
        }
    }

    private fun setDate() {
        val c: Calendar?

        if (mYear == null || mMonth == null || mDayOfMonth == null) {
            c = Calendar.getInstance()
            c?.let { cal ->
                mYear = cal.get(Calendar.YEAR)
                mMonth = cal.get(Calendar.MONTH)
                mDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
            }
        }

        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTheme(R.style.ThemeOverlay_App_DatePicker)
            .build()

        materialDatePicker.show(childFragmentManager, EMPTY_STRING)

        materialDatePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = Date(it)

            mYear = calendar.get(Calendar.YEAR)
            mMonth = calendar.get(Calendar.MONTH) + 1
            mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            button_fragment_registration_date.text = DATE_TEXT_FORMAT.format(
                mDayOfMonth,
                DateFormatSymbols().months[mMonth!! - 1],
                mYear
            )
        }
    }
}