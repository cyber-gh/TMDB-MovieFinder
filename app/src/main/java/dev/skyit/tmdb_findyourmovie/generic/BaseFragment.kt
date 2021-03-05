package dev.skyit.tmdb_findyourmovie.generic

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

open class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment(layoutId) {
}