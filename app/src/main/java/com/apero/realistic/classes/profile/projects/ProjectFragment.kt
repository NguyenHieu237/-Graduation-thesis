package com.apero.realistic.classes.profile.projects

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.classes.adapter.MyProjectsAdapter
import com.apero.realistic.databinding.FragmentProjectBinding
import com.apero.realistic.model.ArtworkModel
import com.apero.realistic.widget.mainlist.LayoutManagerDependentItemSizeProvider
import com.apero.realistic.widget.mainlist.ListConfigItemViewAdjuster
import com.apero.realistic.widget.mainlist.dpToPx
import com.apero.realistic.widget.mainlist.spacingitemdecoration.Spacing
import com.apero.realistic.widget.mainlist.spacingitemdecoration.SpacingItemDecoration

class ProjectFragment: BaseFragment<FragmentProjectBinding>() {
    private var myProjectsAdapter: MyProjectsAdapter? = null
    private val spacingItemDecoration = SpacingItemDecoration(Spacing())
    private lateinit var itemSizeProvider: LayoutManagerDependentItemSizeProvider
    private val itemViewAdjuster = ListConfigItemViewAdjuster()

    override val layoutId: Int
        get() = R.layout.fragment_project

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupListMyProject()
    }

    override fun setupUI() {
    }

    override fun setupListener() {
    }

    override fun setupViewModel() {
    }

    // TODO Functions
    private fun setupListMyProject() {
        val context = activity.guardLet { return }!!

        myProjectsAdapter = MyProjectsAdapter(
            context = context
        )

        val layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
        binding.rcvMyProjects.setHasFixedSize(true)
        binding.rcvMyProjects.adapter = myProjectsAdapter
        binding.rcvMyProjects.layoutManager = layoutManager
        spacingItemDecoration.spacing = Spacing(
            0,
            0,
            item = Rect(
                resources.dpToPx(8F).toInt(),
                resources.dpToPx(8F).toInt(),
                resources.dpToPx(8F).toInt(),
                resources.dpToPx(8F).toInt()
            )
        )
        binding.rcvMyProjects.addItemDecoration(spacingItemDecoration)

        itemSizeProvider = LayoutManagerDependentItemSizeProvider(layoutManager)
        myProjectsAdapter?.itemSizeProvider = itemSizeProvider
        myProjectsAdapter?.itemViewAdjuster = itemViewAdjuster

        myProjectsAdapter?.setListItems(
            arrayListOf(
                ArtworkModel(),
                ArtworkModel(ratio = 16/9f),
                ArtworkModel(),
                ArtworkModel(ratio = 4/3f),
                ArtworkModel(),
                ArtworkModel(),
                ArtworkModel(),
                ArtworkModel(),
                ArtworkModel(),
                ArtworkModel()
            )
        )
        binding.rcvMyProjects.invalidateItemDecorations()
    }
}