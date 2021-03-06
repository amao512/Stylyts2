package kz.eztech.stylyts.presentation.fragments

import android.app.DatePickerDialog
import android.view.View
import androidx.core.text.HtmlCompat
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_refresh_password.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.include_toolbar
import kotlinx.android.synthetic.main.fragment_registration.include_base_progress
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.models.SharedConstants.TOKEN_KEY
import kz.eztech.stylyts.data.models.SharedConstants.USER_ID_KEY
import kz.eztech.stylyts.domain.models.UserModel
import kz.eztech.stylyts.presentation.activity.AuthorizationActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.RegistrationContract
import kz.eztech.stylyts.presentation.presenters.RegistrationPresenter
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.HashMap

class RegistrationFragment : BaseFragment<AuthorizationActivity>(),RegistrationContract.View, View.OnClickListener {
    private var mYear:Int? = null
    private var mMonth:Int? = null
    private var mDayOfMonth:Int? = null
    @Inject
    lateinit var presenter:RegistrationPresenter
    override fun customizeActionBar() {
        with(include_toolbar){
            image_button_left_corner_action.visibility = View.VISIBLE
            text_view_toolbar_back.visibility = View.VISIBLE
            text_view_toolbar_title.visibility = View.VISIBLE
            image_button_right_corner_action.visibility = View.GONE
            customizeActionToolBar(this,getString(R.string.fragment_registration_appbar_title))
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
        text_view_fragment_registration_term.text =
            HtmlCompat.fromHtml(
                getString(R.string.fragment_registration_term),
                HtmlCompat.FROM_HTML_MODE_LEGACY)

    }

    override fun initializeViews() {}

    override fun initializeListeners() {
        button_fragment_registration_date.setOnClickListener(this)
        button_fragment_registration_submit.setOnClickListener(this)
    }
    
    override fun processPostInitialization() {
    
    }
    
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
        return R.layout.fragment_registration
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.text_view_fragment_registration_term -> {

            }
            R.id.button_fragment_registration_date -> {
                var c: Calendar? = null

                if(mYear==null || mMonth==null || mDayOfMonth == null){
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
                    button_fragment_registration_date.setText("$mDayOfMonth / ${DateFormatSymbols().getMonths()[mMonth!!-1]} / $mYear")

                }, mYear!!, mMonth!!, mDayOfMonth!!)

                dpd.show()
            }

            R.id.button_fragment_registration_submit -> {
                checkData()
            }
        }
    }

    override fun checkData() {
        if(edit_text_view_fragment_registration_name.text.isNotEmpty() &&
            edit_text_view_fragment_registration_last_name.text.isNotEmpty() &&
            edit_text_view_fragment_registration_email.text.isNotEmpty() &&
            edit_text_view_fragment_registration_password.text.isNotEmpty() &&
            edit_text_view_fragment_registration_password_repeat.text.isNotEmpty() &&
            mYear!=null && mMonth!=null && mDayOfMonth != null){
            if( edit_text_view_fragment_registration_password.text.toString() !=
                edit_text_view_fragment_registration_password_repeat.text.toString()){
                displayMessage("???????????? ??????????????????????")
            }else{
                processCheckedData()
            }
        }else{
            displayMessage("?????????????????? ???????????? ????????")
        }
    }

    override fun processCheckedData() {
        val data = HashMap<String,Any>()
        data["email"] = edit_text_view_fragment_registration_email.text.toString()
        data["password"] = edit_text_view_fragment_registration_password.text.toString()
        data["name"] = edit_text_view_fragment_registration_name.text.toString()
        data["last_name"] =  edit_text_view_fragment_registration_last_name.text.toString()
        data["date_of_birth"] = "$mYear-$mMonth-$mDayOfMonth"
        data["should_send_mail"] = true
        data["username"] = edit_text_view_fragment_registration_username.text.toString()

        presenter.registerUser(data)
    }

    override fun processSuccessRegistration() {
        currentActivity.onBackPressed()
        displayMessage("??????????????")
    }

    override fun displayProgress() {
        include_base_progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        include_base_progress.visibility = View.GONE
    }
    
    override fun processUser(user: UserModel) {
        currentActivity.saveSharedPrefByKey(TOKEN_KEY,user.token)
        currentActivity.saveSharedPrefByKey(USER_ID_KEY,user.id)
        processSuccessRegistration()
    }
}