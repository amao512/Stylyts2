package kz.eztech.stylyts.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kz.eztech.stylyts.R
import kz.eztech.stylyts.presentation.base.BaseActivity

class AuthorizationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
    }
}