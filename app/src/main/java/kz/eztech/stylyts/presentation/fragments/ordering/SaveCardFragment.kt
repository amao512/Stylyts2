package kz.eztech.stylyts.presentation.fragments.ordering

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_save_card.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.card.CardEntity
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.ordering.SaveCardContract
import kz.eztech.stylyts.presentation.presenters.ordering.SaveCardPresenter
import kz.eztech.stylyts.presentation.utils.EMPTY_STRING
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show
import java.util.*
import javax.inject.Inject

class SaveCardFragment : BaseFragment<MainActivity>(), SaveCardContract.View, View.OnClickListener {

    @Inject lateinit var presenter: SaveCardPresenter

    private lateinit var cardNumberEditText: EditText
    private lateinit var monthTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var monthAndYearTextView: TextView
    private lateinit var cardNameTextView: EditText
    private lateinit var saveButton: MaterialButton

    private var mYear: Int? = null
    private var mMonth: Int? = null

    companion object {
        const val CURRENT_CARD_ID_KEY = "currentCard"
        const val IS_SUCCESS_CREATING = "isSuccess"
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_save_card

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(fragment_save_card_toolbar) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.setOnClickListener(this@SaveCardFragment)
            toolbar_left_corner_action_image_button.show()

            toolbar_title_text_view.text = getString(R.string.save_card_fragment_title)
            toolbar_title_text_view.show()

            toolbar_bottom_border_view.hide()
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(fragment = this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(CURRENT_CARD_ID_KEY)) {
                presenter.getCardById(it.getInt(CURRENT_CARD_ID_KEY))
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        cardNumberEditText = fragment_save_card_edit_text
        monthTextView = fragment_save_card_month_text_view
        yearTextView = fragment_save_card_year_text_view
        monthAndYearTextView = fragment_save_card_month_and_year_text_view
        cardNameTextView = fragment_save_card_name_in_card_edit_text
        saveButton = fragment_save_card_complete_button
    }

    override fun initializeListeners() {
        monthTextView.setOnClickListener(this)
        yearTextView.setOnClickListener(this)
        monthAndYearTextView.setOnClickListener(this)
        saveButton.setOnClickListener(this)
    }

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {
        displayMessage(msg)
    }

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_left_corner_action_image_button -> findNavController().navigateUp()
            R.id.fragment_save_card_month_text_view -> displayCalendar()
            R.id.fragment_save_card_year_text_view -> displayCalendar()
            R.id.fragment_save_card_month_and_year_text_view -> displayCalendar()
            R.id.fragment_save_card_complete_button -> saveCard()
        }
    }

    override fun processCard(cardEntity: CardEntity) {
        cardNumberEditText.setText(cardEntity.number)
        cardNameTextView.setText(cardEntity.nameHolder)
        monthAndYearTextView.text = cardEntity.expDate
    }

    override fun processSuccessSaving() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set<Boolean>(IS_SUCCESS_CREATING, true)
        findNavController().popBackStack()
    }

    private fun displayCalendar() {
        val c: Calendar?

        if (mYear == null || mMonth == null) {
            c = Calendar.getInstance()
            c?.let { cal ->
                mYear = cal.get(Calendar.YEAR)
                mMonth = cal.get(Calendar.MONTH)
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

            monthAndYearTextView.text = "$mMonth / $mYear"
        }
    }

    private fun saveCard() {
        if (checkEmptyInput()) {
            val card = CardEntity(
                number = cardNumberEditText.text.toString(),
                nameHolder = cardNameTextView.text.toString(),
                expDate = monthAndYearTextView.text.toString()
            )

            presenter.saveCard(card)
        }
    }

    private fun checkEmptyInput(): Boolean {
        var flag = true

        if (cardNumberEditText.text.isBlank() || cardNameTextView.text.isBlank() ||
                monthAndYearTextView.text.isBlank()) {
            flag = false
        }

        if (!flag) {
            displayMessage(msg = getString(R.string.empty_fields_message))
        }

        return flag
    }
}