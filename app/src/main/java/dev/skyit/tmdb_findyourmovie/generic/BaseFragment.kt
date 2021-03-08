package dev.skyit.tmdb_findyourmovie.generic

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import dev.skyit.tmdb_findyourmovie.R

open class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment(layoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.custom_move)
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.custom_move)
    }
}