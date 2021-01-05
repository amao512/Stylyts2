package kz.eztech.stylyts.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

    fun hideBottomNavigationView(){
        bottomNavigationView.visibility = View.GONE
    }
    fun displayBottomNavigationView(){
        bottomNavigationView.visibility = View.VISIBLE
    }
}