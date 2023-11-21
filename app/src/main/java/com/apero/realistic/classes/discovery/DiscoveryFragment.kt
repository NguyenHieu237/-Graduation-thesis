package com.apero.realistic.classes.discovery

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.disposedBy
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.classes.adapter.artwork.ArtworkCategoryAdapter
import com.apero.realistic.classes.adapter.TopTrendingAdapter
import com.apero.realistic.classes.adapter.artwork.ArtworkAdapter
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.FragmentDiscoveryBinding
import com.apero.realistic.manager.GenerateSettingManager
import com.apero.realistic.model.ArtworkCategoryModel
import com.apero.realistic.model.ArtworkModel
import com.apero.realistic.model.TrendingModel
import com.apero.realistic.widget.mainlist.LayoutManagerDependentItemSizeProvider
import com.apero.realistic.widget.mainlist.ListConfigItemViewAdjuster
import com.apero.realistic.widget.mainlist.dpToPx
import com.apero.realistic.widget.mainlist.spacingitemdecoration.Spacing
import com.apero.realistic.widget.mainlist.spacingitemdecoration.SpacingItemDecoration

class DiscoveryFragment : BaseFragment<FragmentDiscoveryBinding>() {
    private val generateSettingManager: GenerateSettingManager = GenerateSettingManager.shared
    private var adapter: TopTrendingAdapter? = null
    private var artworkCategoryAdapter: ArtworkCategoryAdapter? = null
    private var artworkAdapter: ArtworkAdapter? = null
    private val spacingItemDecoration = SpacingItemDecoration(Spacing())
    private lateinit var itemSizeProvider: LayoutManagerDependentItemSizeProvider
    private val itemViewAdjuster = ListConfigItemViewAdjuster()
    private val viewModel: DiscoveryViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_discovery

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupArtwork()
    }

    override fun setupUI() {
        this.setupTopTrending()
        this.setupArtworkCategory()
    }

    override fun setupViewModel() {
        viewModel.onSelectCategoryCompletion = {
            artworkCategoryAdapter?.reloadData()
        }
    }

    override fun setupListener() {
        generateSettingManager.getListTagKeywordObservable()
            .subscribe {

            }
            .disposedBy(bag)
    }

    // Functions
    private fun setupTopTrending() {
        val context = context.guardLet { return }!!
        adapter = TopTrendingAdapter(context = context, listenerCallback = object :
            OnItemClickListener<TrendingModel> {
            override fun onItemClick(position: Int, model: TrendingModel) {
                makeUIDialogTryArtwork()
            }
        })
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcvTopTrending.layoutManager = manager
        binding.rcvTopTrending.adapter = adapter
        adapter?.setListItems(list = viewModel.getListTrending())
    }

    private fun setupArtworkCategory() {
        val context = context.guardLet { return }!!
        artworkCategoryAdapter =
            ArtworkCategoryAdapter(context = context, listenerCallback = object :
                OnItemClickListener<ArtworkCategoryModel> {
                override fun onItemClick(position: Int, model: ArtworkCategoryModel) {
                    viewModel.setArtworkSelected(position = position)
                }
            })
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcvArtworkCategory.layoutManager = manager
        binding.rcvArtworkCategory.adapter = artworkCategoryAdapter
        artworkCategoryAdapter?.setListItems(list = viewModel.getListArtworkCategory())
    }

    private fun setupArtwork() {
        val context = activity.guardLet { return }!!

        artworkAdapter = ArtworkAdapter(
            context = context
        )

        val layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
        binding.rcvArtworks.setHasFixedSize(true)
        binding.rcvArtworks.adapter = artworkAdapter
        binding.rcvArtworks.layoutManager = layoutManager
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
        binding.rcvArtworks.addItemDecoration(spacingItemDecoration)

        itemSizeProvider = LayoutManagerDependentItemSizeProvider(layoutManager)
        artworkAdapter?.itemSizeProvider = itemSizeProvider
        artworkAdapter?.itemViewAdjuster = itemViewAdjuster

        artworkAdapter?.setListItems(
            arrayListOf(
                ArtworkModel(ratio = 9/16f),
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
        binding.rcvArtworks.invalidateItemDecorations()
    }

    // TODO Navigation
    private fun makeUIDialogTryArtwork() {
        Router.makeUIDialogTryArtwork(this, onTryThisCompletion = {
            makeUIResult()
        })
    }

    private fun makeUIResult() {
        val activity = (activity as? BaseActivity<*>).guardLet { return }!!
        Router.makeUIResult(from = activity)
    }
}