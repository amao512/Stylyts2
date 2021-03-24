package kz.eztech.stylyts.common.presentation.fragments.users

import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.base_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_user_comments.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.domain.models.UserComment
import kz.eztech.stylyts.common.presentation.activity.MainActivity
import kz.eztech.stylyts.common.presentation.adapters.UserCommentsAdapter
import kz.eztech.stylyts.common.presentation.base.BaseFragment
import kz.eztech.stylyts.common.presentation.base.BaseView
import kz.eztech.stylyts.common.presentation.contracts.EmptyContract
import kz.eztech.stylyts.common.presentation.utils.extensions.hide
import kz.eztech.stylyts.common.presentation.utils.extensions.show

class UserCommentsFragment : BaseFragment<MainActivity>(), EmptyContract.View {

    private lateinit var adapter: UserCommentsAdapter

    override fun onResume() {
        super.onResume()
        currentActivity.hideBottomNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.fragment_user_comments

    override fun getContractView(): BaseView = this

    override fun customizeActionBar() {
        with(include_toolbar_collection_detail) {
            toolbar_left_corner_action_image_button.show()
            toolbar_back_text_view.show()
            toolbar_title_text_view.show()
            toolbar_right_corner_action_image_button.hide()

            customizeActionToolBar(
                toolbar = this,context.getString(R.string.toolbar_title_comments)
            )
        }
    }

    override fun initializeDependency() {}

    override fun initializePresenter() {}

    override fun initializeArguments() {}

    override fun initializeViewsData() {
        adapter = UserCommentsAdapter()
        val dummyList = ArrayList<UserComment>()
        dummyList.add(
            UserComment(
                1,
                "Anton",
                "Zhakup",
                null,
                "antonidze",
                "04 янв. 2021",
                "Очень круто! 🔥"
            )
        )
        dummyList.add(
            UserComment(
                2,
                "Йоган",
                "Костин",
                null,
                "const",
                "03 янв. 2021",
                "Огоонь! 🔥🔥🔥"
            )
        )
        dummyList.add(
            UserComment(
                3,
                "Рафаил",
                "Некрасов ",
                null,
                "rafchik",
                "02 янв. 2021",
                "То что я люблю 😘😘😘😘"
            )
        )
        dummyList.add(
            UserComment(
                4,
                "Lennox",
                "Gonzales",
                null,
                "gonzales",
                "01 янв. 2021",
                "Cuteee!😁😁😁"
            )
        )
        dummyList.add(
            UserComment(
                5,
                "Wesley",
                "Garcia",
                null,
                "girnay",
                "28 дек. 2021",
                "Nice choise! 👍👍👍"
            )
        )
        dummyList.add(
            UserComment(
                6,
                "Kevin",
                "Lee",
                null,
                "leekev",
                "26 янв. 2021",
                "Высший класс! 😎😎"
            )
        )
        dummyList.add(
            UserComment(
                7,
                "Валериан ",
                "Шуфрич",
                null,
                "asasa",
                "26 янв. 2021",
                "Что то не очень! 😒😒"
            )
        )
        dummyList.add(
            UserComment(
                8,
                "Юлий",
                "Журавлёв",
                null,
                "zhuraleekev",
                "24 янв. 2021",
                "Мда уж.. 🤢🤢🤢"
            )
        )
        dummyList.add(
            UserComment(
                9,
                "Харитон",
                "Бачей",
                null,
                "leekev",
                "26 янв. 2021",
                "Высший класс! 😎😎"
            )
        )
        dummyList.add(
            UserComment(
                10,
                "Anton",
                "Zhakup",
                null,
                "antonidze",
                "04 янв. 2021",
                "Очень круто! 🔥"
            )
        )
        dummyList.add(
            UserComment(
                11,
                "Йоган",
                "Костин",
                null,
                "const",
                "03 янв. 2021",
                "Огоонь! 🔥🔥🔥"
            )
        )
        dummyList.add(
            UserComment(
                12,
                "Рафаил",
                "Некрасов ",
                null,
                "rafchik",
                "02 янв. 2021",
                "То что я люблю 😘😘😘😘"
            )
        )
        dummyList.add(
            UserComment(
                13,
                "Lennox",
                "Gonzales",
                null,
                "gonzales",
                "01 янв. 2021",
                "Cuteee!😁😁😁"
            )
        )
        dummyList.add(
            UserComment(
                14,
                "Lennox",
                "Gonzales",
                null,
                "gonzales",
                "01 янв. 2021",
                "Cuteee!😁😁😁"
            )
        )
        dummyList.add(
            UserComment(
                15,
                "Wesley",
                "Garcia",
                null,
                "girnay",
                "28 дек. 2021",
                "Nice choise! 👍👍👍"
            )
        )
        dummyList.add(
            UserComment(
                16,
                "Kevin",
                "Lee",
                null,
                "leekev",
                "26 янв. 2021",
                "Высший класс! 😎😎"
            )
        )
        dummyList.add(
            UserComment(
                17,
                "Валериан ",
                "Шуфрич",
                null,
                "asasa",
                "26 янв. 2021",
                "Что то не очень! 😒😒"
            )
        )
        dummyList.add(
            UserComment(
                18,
                "Юлий",
                "Журавлёв",
                null,
                "zhuraleekev",
                "24 янв. 2021",
                "Мда уж.. 🤢🤢🤢"
            )
        )
        dummyList.add(
            UserComment(
                19,
                "Харитон",
                "Бачей",
                null,
                "leekev",
                "26 янв. 2021",
                "Высший класс! 😎😎"
            )
        )
        adapter.updateList(dummyList)
    }

    override fun initializeViews() {
        recycler_view_fragment_user_comments.layoutManager = LinearLayoutManager(currentActivity)
        recycler_view_fragment_user_comments.adapter = adapter
    }

    override fun initializeListeners() {}

    override fun processPostInitialization() {}

    override fun disposeRequests() {}

    override fun displayMessage(msg: String) {}

    override fun isFragmentVisible(): Boolean = isVisible

    override fun displayProgress() {}

    override fun hideProgress() {}
}