package com.apero.realistic.classes.splash

import android.annotation.SuppressLint
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.ActivitySplashBinding
import com.apero.realistic.manager.UserSessionManager
import com.apero.realistic.utils.RunAble

@SuppressLint("CustomSplashScreen")
class SplashActivity: BaseActivity<ActivitySplashBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_splash

    override fun setupUI() {

    }

    override fun setupListener() {
        RunAble.runAfter(3000) {
            directUI()
        }
    }

    override fun setupViewModel() {
    }

    // TODO Navigation
    private fun directUI() {
        if (!UserSessionManager.shared.isSelectedLanguage()) {
            Router.makeUILanguage(this, true)
        } else {
            Router.makeUIMainClearTask(this)
        }
    }
}