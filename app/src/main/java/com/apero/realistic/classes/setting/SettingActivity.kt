package com.apero.realistic.classes.setting

import androidx.recyclerview.widget.LinearLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.classes.adapter.SettingAdapter
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.ActivitySettingBinding
import com.apero.realistic.model.SettingModel
import com.apero.realistic.model.Type

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    private lateinit var adapter: SettingAdapter
    override val layoutId: Int
        get() = R.layout.activity_setting

    override fun setupUI() {
        val list: ArrayList<SettingModel> = arrayListOf()
        list.add(SettingModel(R.drawable.ic_language, getString(R.string.language), Type.LANGUAGE))
        list.add(SettingModel(R.drawable.ic_policy, getString(R.string.privacy_policy), Type.PRIVACY_POLICY))
        list.add(SettingModel(R.drawable.ic_terms_of_service, getString(R.string.term_of_service), Type.TERM_OF_SERVICES)
        )
        adapter = SettingAdapter(listenerCallback = object :
            OnItemClickListener<SettingModel> {
            override fun onItemClick(position: Int, model: SettingModel) {
                when (model.type) {
                    Type.LANGUAGE -> {
                        Router.makeUILanguage(this@SettingActivity, false)
                    }
                    Type.PRIVACY_POLICY -> {

                    }
                    Type.TERM_OF_SERVICES -> {

                    }
                }
            }
        })
        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.rcvSetting.layoutManager = manager
        binding.rcvSetting.adapter = adapter
        adapter.setListItems(list = list)
    }

    override fun setupListener() {
        binding.icBack.setOnClickListener {
            finish()
        }

        binding.icProfile.setOnClickListener {

        }
    }

    override fun setupViewModel() {

    }

}
