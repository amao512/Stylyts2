package kz.eztech.stylyts.global.presentation.card.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_card.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.global.data.db.card.CardEntity
import kz.eztech.stylyts.global.presentation.common.ui.MainActivity
import kz.eztech.stylyts.global.presentation.common.ui.adapters.CardAdapter
import kz.eztech.stylyts.global.presentation.base.BaseFragment
import kz.eztech.stylyts.global.presentation.base.BaseView
import kz.eztech.stylyts.global.presentation.card.contracts.CardContract
import kz.eztech.stylyts.global.presentation.common.interfaces.UniversalViewClickListener
import kz.eztech.stylyts.global.presentation.card.presenters.CardPresenter
import kz.eztech.stylyts.utils.extensions.hide
import kz.eztech.stylyts.utils.extensions.show
import javax.inject.Inject

class CardFragment : BaseFragment<MainActivity>(), CardContract.View, View.OnClickListener,
    UniversalViewClickListener {

    @Inject lateinit var presenter: CardPresenter
    private lateinit var cardAdapter: CardAdapter

    private var currentMode = DEFAULT_MODE

    companion object {
        const val MODE_KEY = "mode"
        const val DEFAULT_MODE = 1
        const val GET_CARD_MODE = 2
        const val CARD_KEY = "card"
    }

    override fun getLayoutId(): Int = R.layout.fragment_card

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_card) {
            toolbar_left_corner_action_image_button.setImageResource(R.drawable.ic_baseline_keyboard_arrow_left_24)
            toolbar_left_corner_action_image_button.show()
            toolbar_title_text_view.show()

            customizeActionToolBar(toolbar = this, title = getString(R.string.card_fragment_add_way_payment))
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {
        presenter.attach(view = this)
    }

    override fun initializeArguments() {
        arguments?.let {
            if (it.containsKey(MODE_KEY)) {
                currentMode = it.getInt(MODE_KEY)
            }
        }
    }

    override fun initializeViewsData() {}

    override fun initializeViews() {
        cardAdapter = CardAdapter()
        cardAdapter.setOnClickListener(listener = this)
    }

    override fun initializeListeners() {
        linear_layout_fragment_card_profile_add_card.setOnClickListener(this)
        recycler_view_fragment_card.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_card.adapter = cardAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.linear_layout_fragment_card_profile_add_card -> {
                findNavController().navigate(R.id.action_cardFragment_to_saveCardFragment)
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when (item) {
            is CardEntity -> {
                when (currentMode) {
                    DEFAULT_MODE -> navigateToSaveCard(item)
                    GET_CARD_MODE -> returnCardToBack(item)
                }
            }
        }
    }

    override fun processPostInitialization() {
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Boolean>(SaveCardFragment.IS_SUCCESS_CREATING)
            ?.observe(viewLifecycleOwner, {
                if (it) {
                    presenter.getCardList()
                }
            })

        presenter.getCardList()
    }

    override fun disposeRequests() {
        presenter.disposeRequests()
    }

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun processCards(list: List<CardEntity>) {
        if (list.isEmpty()) {
            showEmptyPage()
        } else {
            cardAdapter.updateList(list)
            hideEmptyPage()
        }
    }

//    private fun displayCalendar() {
//        var c: Calendar? = null
//
//        if (mYear == null || mMonth == null) {
//            c = Calendar.getInstance()
//            c?.let { cal ->
//                mYear = cal.get(Calendar.YEAR)
//                mMonth = cal.get(Calendar.MONTH)
//                mDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
//            }
//        }
//
//        val dpd = DatePickerDialog(currentActivity, { view, year, monthOfYear, dayOfMonth ->
//            mYear = year
//            mMonth = monthOfYear
//            mDayOfMonth = dayOfMonth
//            text_view_fragment_card_expiring.setText("$mMonth / $mYear")
//
//        }, mYear!!, mMonth!!, mDayOfMonth!!)
//        val dayView = dpd.datePicker.findViewById<View>(
//            Resources.getSystem().getIdentifier("android:id/day", null, null)
//        )
//        dayView?.let {
//            it.visibility = View.GONE
//        }
//        dpd.show()
//    }

    private fun navigateToSaveCard(cardEntity: CardEntity) {
        val bundle = Bundle()
        cardEntity.id?.let {
            bundle.putInt(SaveCardFragment.CURRENT_CARD_ID_KEY, it)
        }

        findNavController().navigate(R.id.action_cardFragment_to_saveCardFragment, bundle)
    }

    private fun returnCardToBack(cardEntity: CardEntity) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set<Int>(CARD_KEY, cardEntity.id)
        findNavController().popBackStack()
    }

    private fun showEmptyPage() {
        text_view_fragment_card_profile_empty.show()
    }

    private fun hideEmptyPage() {
        text_view_fragment_card_profile_empty.hide()
    }
}
