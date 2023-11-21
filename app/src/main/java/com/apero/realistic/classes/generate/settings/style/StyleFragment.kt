package com.apero.realistic.classes.generate.settings.style

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.classes.adapter.StyleAdapter
import com.apero.realistic.databinding.FragmentStyleBinding
import com.apero.realistic.model.StyleModel

class StyleFragment: BaseFragment<FragmentStyleBinding>() {
    private var styleAdapter: StyleAdapter? = null

    override val layoutId: Int
        get() = R.layout.fragment_style

    override fun setupUI() {
        this.setupStyle()
    }

    override fun setupListener() {

    }

    override fun setupViewModel() {
    }

    // TODO Functions
    private fun setupStyle() {
        val context = context.guardLet { return }!!
        val listStyle: ArrayList<StyleModel> = arrayListOf(
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel(),
            StyleModel()

        )
        styleAdapter = StyleAdapter(context = context, listenerCallback = object :
            OnItemClickListener<StyleModel> {
            override fun onItemClick(position: Int, model: StyleModel) {

            }
        })
        val manager = GridLayoutManager(context, 3)
        manager.orientation = LinearLayoutManager.VERTICAL
        binding.rcvStyle.layoutManager = manager
        binding.rcvStyle.adapter = styleAdapter
        styleAdapter?.setListItems(list = listStyle)
    }
}