package kz.eztech.stylyts.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import kz.eztech.stylyts.R
import kz.eztech.stylyts.data.models.SharedConstants
import kz.eztech.stylyts.presentation.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_scale_in)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val currentToken = getSharedPrefByKey<String>(SharedConstants.TOKEN_KEY)
//                if (currentToken.isNullOrEmpty()) {
//                    startActivity(Intent(this@SplashActivity, AuthorizationActivity::class.java))
//                    finish()
//                } else {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
//                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        text_view_activity_splash_title.startAnimation(animation)
    }
}