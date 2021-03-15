package dev.skyit.tmdb_findyourmovie.ui.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.skyit.tmdb_findyourmovie.R

fun RecyclerView.setItemSpacing() {
    val spacing =
        resources.getDimensionPixelSize(R.dimen.recycler_view_double_column_spacing) / 2
    this.setPadding(spacing, spacing, spacing, spacing)
    this.clipToPadding = false
    this.clipChildren = false

    this.addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.set(spacing, spacing, spacing, spacing)
        }
    })
}