package com.apero.realistic.classes.generate

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.disposedBy
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.base.ext.rxTextChange
import com.apero.realistic.classes.adapter.InspirationAdapter
import com.apero.realistic.classes.adapter.RatioAdapter
import com.apero.realistic.classes.bottomsheet.GenerateSettingKey
import com.apero.realistic.classes.bottomsheet.GenerateSettingUI
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.FragmentGenerateBinding
import com.apero.realistic.manager.GenerateSettingManager
import com.apero.realistic.model.InspirationModel
import com.apero.realistic.model.RatioModel

class GenerateFragment : BaseFragment<FragmentGenerateBinding>() {
    private var generateSettingManager: GenerateSettingManager = GenerateSettingManager.shared
    private var adapter: RatioAdapter? = null
    private var inspirationAdapter: InspirationAdapter? = null
    private val viewModel: GenerateViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_generate

    override fun setupUI() {
        this.setupRatio()
        this.setupInspirations()
    }

    @SuppressLint("SetTextI18n")
    override fun setupListener() {
        binding.edtPrompt.rxTextChange()
            .map { it.count() }
            .subscribe {
                binding.tvPromptCount.text = (if (it < 10) "0" else "") + it.toString()
            }
            .disposedBy(bag)

        binding.lnPromptHistoryChild.setOnClickListener {
            Router.makeUIBottomSheetPromptHistory(from = this@GenerateFragment)
        }

        binding.lnSettingKeyword.setOnClickListener {
            Router.makeUIBottomSheetGenerateSetting(
                from = this@GenerateFragment,
                settings = createListSetting(key = GenerateSettingKey.KEY_WORD)
            )
        }

        binding.lnSettingStyle.setOnClickListener {
            Router.makeUIBottomSheetGenerateSetting(
                from = this@GenerateFragment,
                settings = createListSetting(key = GenerateSettingKey.STYLE)
            )
        }

        binding.lnSettingMore.setOnClickListener {
            Router.makeUIBottomSheetGenerateSetting(
                from = this@GenerateFragment,
                settings = createListSetting(key = GenerateSettingKey.MORE)
            )
        }

        binding.lnGenerateAction.setOnClickListener {
            makeUILoading()
        }

        generateSettingManager.getListInspirationObservable()
            .subscribe {

            }
            .disposedBy(bag)
    }

    override fun setupViewModel() {
        viewModel.onSelectRatioCompletion = {
            adapter?.reloadData()
        }
    }

    // TODO Functions
    private fun setupRatio() {
        val context = context.guardLet { return }!!
        adapter = RatioAdapter(context = context, listenerCallback = object :
            OnItemClickListener<RatioModel> {
            override fun onItemClick(position: Int, model: RatioModel) {
                viewModel.setRatioSelected(position = position)
            }
        })
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcvRatio.layoutManager = manager
        binding.rcvRatio.adapter = adapter
        adapter?.setListItems(list = viewModel.getListRatio())
    }

    private fun setupInspirations() {
        val context = context.guardLet { return }!!
        inspirationAdapter = InspirationAdapter(
            context = context,
            onAddInspirationCompletion = object : OnItemClickListener<InspirationModel> {
                override fun onItemClick(position: Int, model: InspirationModel) {

                }
            })
        val manager = GridLayoutManager(context, 2)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.rcvInspirations.layoutManager = manager
        binding.rcvInspirations.adapter = inspirationAdapter
        inspirationAdapter?.setListItems(
            list = arrayListOf(
                InspirationModel(),
                InspirationModel(),
                InspirationModel(),
                InspirationModel(),
                InspirationModel(),
                InspirationModel(),
                InspirationModel(),
                InspirationModel()
            )
        )
    }

    private fun createListSetting(key: GenerateSettingKey): ArrayList<GenerateSettingUI> {
        val settingsUI: ArrayList<GenerateSettingUI> = arrayListOf(
            GenerateSettingUI(GenerateSettingKey.KEY_WORD, false),
            GenerateSettingUI(GenerateSettingKey.STYLE, false),
            GenerateSettingUI(GenerateSettingKey.MORE, false)
        )
        settingsUI.firstOrNull { it.key == key }?.selected = true
        return settingsUI
    }

    // TODO Navigation
    private fun makeUIResult() {
        val activity = (activity as? BaseActivity<*>).guardLet { return }!!
        Router.makeUIResult(from = activity)
    }

    private fun makeUILoading() {
        val activity = (activity as? BaseActivity<*>).guardLet { return }!!
        Router.makeUIDialogLoading(activity)
    }
}