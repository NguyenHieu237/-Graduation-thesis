package com.apero.realistic.widget.mainlist

import android.app.Activity
import android.graphics.Point
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.utils.ScreenUtil
import java.util.*
import kotlin.math.roundToInt


/**
 * Provides dimensions of list item.
 */
interface ItemSizeProvider {
    /**
     * Determine dimensions of item view and fill passed [size] with expected item's with and height.
     */
    fun provideItemSize(size: Point, itemView: View, position: Int, ratio: Float, activity: Activity)
}

/**
 * Choose item dimensions basing on [layoutManager] properties. [seed] is used to randomize
 * values.
 */
class LayoutManagerDependentItemSizeProvider(
        var layoutManager: RecyclerView.LayoutManager,
        val seed: Long = System.currentTimeMillis()
) : ItemSizeProvider {

    private val random = Random()

    override fun provideItemSize(size: Point, itemView: View, position: Int, ratio: Float, activity: Activity) {
        val regularItemWidth = itemView.resources.dimenPx(R.dimen.list_preview_item_width)
        val regularItemHeight = itemView.resources.dimenPx(R.dimen.list_preview_item_height)

        val lm = layoutManager
        when (lm) {
            is LinearLayoutManager -> {
                // Linear or Grid
                if (lm.orientation == OrientationHelper.VERTICAL) {
                    size.set(ViewGroup.LayoutParams.MATCH_PARENT, regularItemHeight)
                } else {
                    size.set(regularItemWidth, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
            is StaggeredGridLayoutManager -> {
                val length = determineLengthOfStaggeredGridItem(itemView, position, ratio, activity)
                if (lm.orientation == OrientationHelper.VERTICAL) {
                    size.set(ViewGroup.LayoutParams.MATCH_PARENT, length)
                } else {
                    size.set(length, ViewGroup.LayoutParams.MATCH_PARENT)
                }
            }
            else -> {
                size.set(regularItemWidth, regularItemHeight)
            }
        }
    }

    private fun determineLengthOfStaggeredGridItem(itemView: View, position: Int, ratio: Float, activity: Activity): Int {
        // using bitmask instead of nextInt(16) for better performance and more interesting results, regardless of distribution
        val listWidth = ScreenUtil.getScreenWidth(activity).width - 16 * 3
        val itemWidth = (listWidth / 2) * (1 / ratio)
        return itemWidth.roundToInt()
//        val x = 8 + (generatePseudorandomFor(position) and 0x0F)
//        return (x * itemView.resources.dpToPxInt(8))
    }

    private fun generatePseudorandomFor(position: Int): Int {
        return random.apply { setSeed(seed + position) }.nextInt()
    }
}