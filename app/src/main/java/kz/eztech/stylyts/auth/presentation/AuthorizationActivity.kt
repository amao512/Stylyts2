package kz.eztech.stylyts.auth.presentation

import android.os.Bundle
import kz.eztech.stylyts.R
import kz.eztech.stylyts.common.presentation.base.BaseActivity

class AuthorizationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
    }
}