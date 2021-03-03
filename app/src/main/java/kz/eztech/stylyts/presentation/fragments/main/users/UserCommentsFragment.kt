package kz.eztech.stylyts.presentation.fragments.main.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_user_comments.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.domain.models.UserComment
import kz.eztech.stylyts.presentation.activity.MainActivity
import kz.eztech.stylyts.presentation.adapters.UserCommentsAdapter
import kz.eztech.stylyts.presentation.base.BaseFragment
import kz.eztech.stylyts.presentation.base.BaseView
import kz.eztech.stylyts.presentation.contracts.EmptyContract

class UserCommentsFragment : BaseFragment<MainActivity>(),EmptyContract.View {

    private lateinit var adapter:UserCommentsAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_user_comments
    }

    override fun getContractView(): BaseView {
        return this
    }

    override fun customizeActionBar() {
        with(include_toolbar_collection_detail){
            image_button_left_corner_action.visibility = View.VISIBLE
            text_view_toolbar_back.visibility = View.VISIBLE
            text_view_toolbar_title.visibility = View.VISIBLE
            image_button_right_corner_action.visibility = View.GONE
            customizeActionToolBar(this,"Комментарии")
        }
    }

    override fun initializeDependency() {

    }

    override fun initializePresenter() {

    }

    override fun initializeArguments() {

    }

    override fun initializeViewsData() {
        adapter = UserCommentsAdapter()
        val dummyList = ArrayList<UserComment>()
        dummyList.add(UserComment(1,"Anton","Zhakup",null,"antonidze","04 янв. 2021","Очень круто! 🔥"))
        dummyList.add(UserComment(2,"Йоган","Костин",null,"const","03 янв. 2021","Огоонь! 🔥🔥🔥"))
        dummyList.add(UserComment(3,"Рафаил","Некрасов ",null,"rafchik","02 янв. 2021","То что я люблю 😘😘😘😘"))
        dummyList.add(UserComment(4,"Lennox","Gonzales",null,"gonzales","01 янв. 2021","Cuteee!😁😁😁"))
        dummyList.add(UserComment(5,"Wesley","Garcia",null,"girnay","28 дек. 2021","Nice choise! 👍👍👍"))
        dummyList.add(UserComment(6,"Kevin","Lee",null,"leekev","26 янв. 2021","Высший класс! 😎😎"))
        dummyList.add(UserComment(7,"Валериан ","Шуфрич",null,"asasa","26 янв. 2021","Что то не очень! 😒😒"))
        dummyList.add(UserComment(8,"Юлий","Журавлёв",null,"zhuraleekev","24 янв. 2021","Мда уж.. 🤢🤢🤢"))
        dummyList.add(UserComment(9,"Харитон","Бачей",null,"leekev","26 янв. 2021","Высший класс! 😎😎"))
        dummyList.add(UserComment(10,"Anton","Zhakup",null,"antonidze","04 янв. 2021","Очень круто! 🔥"))
        dummyList.add(UserComment(11,"Йоган","Костин",null,"const","03 янв. 2021","Огоонь! 🔥🔥🔥"))
        dummyList.add(UserComment(12,"Рафаил","Некрасов ",null,"rafchik","02 янв. 2021","То что я люблю 😘😘😘😘"))
        dummyList.add(UserComment(13,"Lennox","Gonzales",null,"gonzales","01 янв. 2021","Cuteee!😁😁😁"))
        dummyList.add(UserComment(14,"Lennox","Gonzales",null,"gonzales","01 янв. 2021","Cuteee!😁😁😁"))
        dummyList.add(UserComment(15,"Wesley","Garcia",null,"girnay","28 дек. 2021","Nice choise! 👍👍👍"))
        dummyList.add(UserComment(16,"Kevin","Lee",null,"leekev","26 янв. 2021","Высший класс! 😎😎"))
        dummyList.add(UserComment(17,"Валериан ","Шуфрич",null,"asasa","26 янв. 2021","Что то не очень! 😒😒"))
        dummyList.add(UserComment(18,"Юлий","Журавлёв",null,"zhuraleekev","24 янв. 2021","Мда уж.. 🤢🤢🤢"))
        dummyList.add(UserComment(19,"Харитон","Бачей",null,"leekev","26 янв. 2021","Высший класс! 😎😎"))
        adapter.updateList(dummyList)
    }

    override fun initializeViews() {
        recycler_view_fragment_user_comments.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_user_comments.adapter = adapter
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

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }
}