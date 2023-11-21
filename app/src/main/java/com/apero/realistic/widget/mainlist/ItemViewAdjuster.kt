package com.apero.realistic.widget.mainlist

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import java.util.*

/**
 * Allows to adjust list item view.
 */
interface ItemViewAdjuster {
//    fun adjustListItemViewOnCreate(itemView: View)
    fun adjustListItemViewOnBind(itemView: View, position: Int)
}

/**
 * Adjust list item view to be suitable for specific [listLayoutConfig]. [seed] is used to randomize
 * results.
 */
class ListConfigItemViewAdjuster(
        var listLayoutConfig: ListLayoutConfig? = null,
        val seed: Long = System.currentTimeMillis()
) : ItemViewAdjuster {

    private val random = Random()

    override fun adjustListItemViewOnBind(itemView: View, position: Int) {
        if (listLayoutConfig?.layoutType == ListLayoutConfig.LAYOUT_TYPE_STAGGERED_GRID) {
            val layoutParams = itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams
            val allowItemFullSpan = listLayoutConfig?.allowItemSpan ?: false
            layoutParams?.isFullSpan = allowItemFullSpan && isStaggeredGridItemFullSpan(position)
        }
    }

    private fun isStaggeredGridItemFullSpan(position: Int): Boolean {
        return (random.apply { setSeed(seed + position) }.nextInt() and 0x1F) == 0
    }

}