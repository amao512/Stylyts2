package kz.eztech.stylyts.presentation.fragments.main.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user_subs_item.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.UserComment
import kz.eztech.stylyts.domain.models.UserSub
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserSubAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract

class UserSubsItemFragment : BaseFragment<MainActivity>(),EmptyContract.View {
    private lateinit var adapter:UserSubAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_user_subs_item
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {

    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {
        adapter = UserSubAdapter()
        val dummyList = ArrayList<UserSub>()
        dummyList.add(UserSub(1,"Anton","Zhakup",null,"antonidze"))
        dummyList.add(UserSub(2,"Йоган","Костин",null,"const"))
        dummyList.add(UserSub(3,"Рафаил","Некрасов ",null,"rafchik"))
        dummyList.add(UserSub(4,"Lennox","Gonzales",null,"gonzales"))
        dummyList.add(UserSub(5,"Wesley","Garcia",null,"girnay"))
        dummyList.add(UserSub(6,"Kevin","Lee",null,"leekev"))
        dummyList.add(UserSub(7,"Валериан ","Шуфрич",null,"asasa"))
        dummyList.add(UserSub(8,"Юлий","Журавлёв",null,"zhuraleekev"))
        dummyList.add(UserSub(9,"Харитон","Бачей",null,"leekev"))
        adapter.updateList(dummyList)
    }

    override fun initializeViews() {
        recycler_view_fragment_user_subs_item.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_user_subs_item.adapter = adapter
    }

    override fun initializeListeners() {

    }

    override fun processPostInitialization() {

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
}