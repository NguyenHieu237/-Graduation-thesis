package com.apero.realistic.classes.generate.settings.keyword

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.classes.adapter.TagCategoryAdapter
import com.apero.realistic.databinding.FragmentTagKeywordBinding
import com.apero.realistic.model.TagCategoryModel

class TagKeywordFragment: BaseFragment<FragmentTagKeywordBinding>() {
    private var tagCategoryAdapter: TagCategoryAdapter? = null
    private val viewModel: TagKeywordViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_tag_keyword

    override fun setupUI() {
        this.setupTagCategory()
        this.setupTags()
    }

    override fun setupListener() {
        binding.imgTrash.setOnClickListener {

        }
    }

    override fun setupViewModel() {
        viewModel.onLoadListKeywordCategoryCompletion = {
            tagCategoryAdapter?.setListItems(it)
        }

        viewModel.onSelectKeywordCategoryCompletion = {
            tagCategoryAdapter?.reloadData()
        }

        viewModel.startFetchDataCategory()
    }

    // TODO Functions
    private fun setupTagCategory() {
        val context = context.guardLet { return }!!
        tagCategoryAdapter = TagCategoryAdapter(context = context, listenerCallback = object :
            OnItemClickListener<TagCategoryModel> {
            override fun onItemClick(position: Int, model: TagCategoryModel) {
                viewModel.selectKeywordCategory(position)
            }
        })
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcvTagCategory.layoutManager = manager
        binding.rcvTagCategory.adapter = tagCategoryAdapter
    }

    private fun setupTags() {
        binding.tagView.addTags(
            arrayListOf(
                "All", "CG", "Tokyo Ghoul", "Made in Abysss", "Eget elementum tincidunt in libero"
            )
        )
    }
}