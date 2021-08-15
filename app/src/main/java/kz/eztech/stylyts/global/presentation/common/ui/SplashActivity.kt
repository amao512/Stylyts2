package kz.eztech.stylyts.global.presentation.common.ui

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.auth.presentation.ui.AuthorizationActivity
import kz.eztech.stylyts.global.data.constants.SharedConstants
import kz.eztech.stylyts.global.presentation.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_scale_in)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val currentToken = getSharedPrefByKey<String>(SharedConstants.ACCESS_TOKEN_KEY)
                if (currentToken.isNullOrEmpty()) {
                    startActivity(Intent(this@SplashActivity, AuthorizationActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        activity_splash_logo_image_view.startAnimation(animation)
    }
}