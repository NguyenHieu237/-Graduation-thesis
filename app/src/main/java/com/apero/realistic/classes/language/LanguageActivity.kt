package com.apero.realistic.classes.language

import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.classes.adapter.LanguageAdapter
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.ActivityLanguageBinding

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private val viewModel: LanguageViewModel by viewModels()
    private lateinit var adapter: LanguageAdapter

    override val layoutId: Int
        get() = R.layout.activity_language

    override fun setupUI() {
        this.setupRecyclerview()
    }

    override fun setupListener() {
        binding.btnConfirm.setOnClickListener {
            onConfirmHandler()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun setupViewModel() {
        viewModel.successFetchLanguages.observe(this) {
            adapter.setListItems(it)
        }

        viewModel.startFetchListLanguage()
    }

    // TODO Functions
    private fun setupRecyclerview() {
        adapter = LanguageAdapter()
        adapter.setOnItemClickListener {
            viewModel.chooseLanguage(it)
        }
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter
    }

    private fun onConfirmHandler() {
        if (viewModel.saveLanguage()) {
            makeUIMain()
        } else {
            Toast.makeText(this, getString(R.string.choose_language_warning), Toast.LENGTH_SHORT)
                .show()
        }
    }

    // TODO Navigation
    private fun makeUIMain() {
        Router.makeUIMainClearTask(this)
    }
}
