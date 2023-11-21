package com.apero.realistic.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.apero.realistic.manager.UserSessionManager
import com.apero.realistic.utils.ContextUtils
import com.apero.realistic.utils.SystemUtil
import io.reactivex.disposables.CompositeDisposable
import java.util.*

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    protected abstract val layoutId: Int
    protected lateinit var binding: T
    protected val bag: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupData()
        setupUI()
        setupListener()
        setupViewModel()
        setupNavigationSystemBar()
    }

    override fun onDestroy() {
        bag.clear()
        bag.dispose()
        super.onDestroy()
    }

    protected open fun initBinding() {
        binding = createBinding()
    }

    private fun createBinding(): T {
        return DataBindingUtil.setContentView(this, layoutId)
    }

    protected open fun setupData() {}
    protected abstract fun setupUI()
    protected abstract fun setupListener()
    protected abstract fun setupViewModel()

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { ctx ->
            val locale = UserSessionManager.shared.getLanguageCode()
            val languageTag = Locale.forLanguageTag(locale)
            val localeUpdatedContext = ContextUtils.updateLocale(ctx, languageTag)
            super.attachBaseContext(localeUpdatedContext)
        }
    }

    private fun updateResources(
        context: Context?, language: String
    ): Context? {
        val contextFormatted: Context?
        val locale = Locale(language)
        Locale.setDefault(locale)
        val res = context?.resources
        val config = Configuration(res?.configuration)
        config.setLocale(locale)
        contextFormatted = context?.createConfigurationContext(config)
        return contextFormatted
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        SystemUtil.hideSystemNavigationBar(window)
    }

    @Suppress("DEPRECATION")
    private fun setupNavigationSystemBar() {
        val flags: Int = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )

        window.decorView.systemUiVisibility = flags

        // Code below is to handle presses of Volume up or Volume down.
        // Without this, after pressing volume buttons, the navigation bar will
        // show up and won't hide
        val decorView = window.decorView
        decorView
            .setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = flags
                }
            }
    }
}
