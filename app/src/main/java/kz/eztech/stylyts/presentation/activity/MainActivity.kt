package kz.eztech.stylyts.presentation.activity

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseActivity
import kz.eztech.stylyts.presentation.utils.extensions.hide
import kz.eztech.stylyts.presentation.utils.extensions.show

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

    fun hideBottomNavigationView() {
        bottomNavigationView.hide()
    }

    fun displayBottomNavigationView() {
        bottomNavigationView.show()
    }
}