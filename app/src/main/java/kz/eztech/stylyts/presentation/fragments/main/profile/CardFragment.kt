package kz.eztech.stylyts.presentation.fragments.main.profile

import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_address_profile.*
import kotlinx.android.synthetic.main.fragment_card.*
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_user_comments.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.StylytsApp
import kz.eztech.stylyts.data.db.LocalDataSource
import kz.eztech.stylyts.data.db.entities.AddressEntity
import kz.eztech.stylyts.data.db.entities.CardEntity
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.AddressAdapter
import kz.eztech.stylyts.presentation.adapters.CardAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.main.profile.CardContract
import kz.eztech.stylyts.presentation.interfaces.UniversalViewClickListener
import java.text.DateFormatSymbols
import java.util.*
import javax.inject.Inject

class CardFragment : BaseFragment<MainActivity>(),CardContract.View,View.OnClickListener,UniversalViewClickListener{
    @Inject
    lateinit var ds: LocalDataSource
    private var disposables = CompositeDisposable()
    private var cardAdapter : CardAdapter? = null
    private var currentCard: CardEntity? = null
    private var isForm = false
    private var mYear:Int? = null
    private var mMonth:Int? = null
    private var mDayOfMonth:Int? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_card
    }

    override fun getContractView(): BaseView {
       return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_card){
            image_button_left_corner_action.visibility = View.VISIBLE
            text_view_toolbar_back.visibility = View.VISIBLE
            text_view_toolbar_title.visibility = View.VISIBLE
            text_view_toolbar_title.text = "Добавить Способ Оплаты"
            text_view_toolbar_back.setOnClickListener {
                if(isForm){
                    displayContent()
                }else{
                    findNavController().navigateUp()
                }
            }
            image_button_right_corner_action.visibility = View.GONE
        }
    }

    override fun initializeDependency() {
        (currentActivity.application as StylytsApp).applicationComponent.inject(this)
    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {

    }

    override fun initializeViews() {
        cardAdapter = CardAdapter()
        cardAdapter?.itemClickListener = this


    }

    override fun initializeListeners() {
        linear_layout_fragment_card_profile_add_card.setOnClickListener(this)
        button_fragment_card_submit.setOnClickListener(this)
        recycler_view_fragment_card.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_card.adapter = cardAdapter
        card_date.setOnClickListener(this)
    }

    private fun processList(list:List<CardEntity>){
        cardAdapter?.updateList(list)
        cardAdapter?.notifyDataSetChanged()
    }

    private fun displayExistForm(entity:CardEntity){
        isForm = true
        displayForm()

        edit_text_fragment_card_number.setText(entity.number)
        text_view_fragment_card_expiring.setText(entity.exp_date)
        edit_text_fragment_card_holder.setText(entity.name_holder)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.linear_layout_fragment_card_profile_add_card -> {
                displayForm()
            }
            R.id.button_fragment_card_submit -> {
                disposables.clear()
                currentCard?.let {
                    it.apply {
                        this.number = edit_text_fragment_card_number.text.toString()
                        this.exp_date = text_view_fragment_card_expiring.text.toString()
                        this.name_holder = edit_text_fragment_card_holder.text.toString()
                    }
                    disposables.add(
                        ds.updateCard(it).
                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                            processPostInitialization()
                        }
                    )
                } ?: run{
                    disposables.add(
                        ds.insertCard(CardEntity(
                            number = edit_text_fragment_card_number.text.toString(),
                            exp_date = text_view_fragment_card_expiring.text.toString(),
                            name_holder = edit_text_fragment_card_holder.text.toString(),
                        )).
                        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                            processPostInitialization()
                        }
                    )
                }
            }
            R.id.card_date ->{
                var c: Calendar? = null

                if(mYear==null || mMonth==null ){
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
                    text_view_fragment_card_expiring.setText("$mMonth / $mYear")

                }, mYear!!, mMonth!!, mDayOfMonth!!)
                val dayView = dpd.datePicker.findViewById<View>(Resources.getSystem().getIdentifier("android:id/day", null, null))
                dayView?.let {
                    it.visibility = View.GONE
                }
                dpd.show()
            }
        }
    }

    override fun onViewClicked(view: View, position: Int, item: Any?) {
        when(item){
            is CardEntity -> {
                currentCard = item
                displayExistForm(item)
            }
        }
    }

    override fun processPostInitialization() {
        displayContent()
        getList()
    }

    override fun disposeRequests() {

    }

    override fun displayMessage(msg: String) {

    }

    override fun isFragmentVisible(): Boolean {
        return isVisible
    }

    override fun displayProgress() {

    }

    override fun hideProgress() {

    }

    override fun displayContent() {
        isForm = false
        linear_layout_fragment_card_profile_cards.visibility = View.VISIBLE
        scrollview_fragment_card_form.visibility = View.GONE
    }

    override fun displayForm() {
        isForm = true
        linear_layout_fragment_card_profile_cards.visibility = View.GONE
        scrollview_fragment_card_form.visibility = View.VISIBLE
    }

    private fun showEmptyPage(){
        text_view_fragment_card_profile_empty.visibility = View.VISIBLE
    }

    private fun hideEmptyPage(){
        text_view_fragment_card_profile_empty.visibility = View.GONE
    }

    private fun getList(){
        displayProgress()
        disposables.clear()
        disposables.add(
            ds.allCards.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                hideProgress()
                if(it.isEmpty()){
                    showEmptyPage()
                }else{
                    hideEmptyPage()
                    processList(it)
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }
}
