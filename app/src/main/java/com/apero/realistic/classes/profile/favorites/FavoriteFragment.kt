package com.apero.realistic.classes.profile.favorites

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.classes.adapter.artwork.ArtworkAdapter
import com.apero.realistic.databinding.FragmentFavoriteBinding
import com.apero.realistic.model.ArtworkModel
import com.apero.realistic.widget.mainlist.LayoutManagerDependentItemSizeProvider
import com.apero.realistic.widget.mainlist.ListConfigItemViewAdjuster
import com.apero.realistic.widget.mainlist.dpToPx
import com.apero.realistic.widget.mainlist.spacingitemdecoration.Spacing
import com.apero.realistic.widget.mainlist.spacingitemdecoration.SpacingItemDecoration

class FavoriteFragment: BaseFragment<FragmentFavoriteBinding>() {
    private var artworkAdapter: ArtworkAdapter? = null
    private val spacingItemDecoration = SpacingItemDecoration(Spacing())
    private lateinit var itemSizeProvider: LayoutManagerDependentItemSizeProvider
    private val itemViewAdjuster = ListConfigItemViewAdjuster()

    override val layoutId: Int
        get() = R.layout.fragment_favorite

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupListFavorite()
    }

    override fun setupUI() {
    }

    override fun setupListener() {
    }

    override fun setupViewModel() {
    }

    // TODO Functions
    private fun setupListFavorite() {
        val context = activity.guardLet { return }!!

        artworkAdapter = ArtworkAdapter(
            context = context
        )

        val layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
        binding.rcvMyFavorites.setHasFixedSize(true)
        binding.rcvMyFavorites.adapter = artworkAdapter
        binding.rcvMyFavorites.layoutManager = layoutManager
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
        binding.rcvMyFavorites.addItemDecoration(spacingItemDecoration)

        itemSizeProvider = LayoutManagerDependentItemSizeProvider(layoutManager)
        artworkAdapter?.itemSizeProvider = itemSizeProvider
        artworkAdapter?.itemViewAdjuster = itemViewAdjuster

        artworkAdapter?.setListItems(
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
        binding.rcvMyFavorites.invalidateItemDecorations()
    }
}